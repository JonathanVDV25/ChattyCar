package be.vinci.ipl.chattycar.gateway;

import be.vinci.ipl.chattycar.gateway.models.*;
import java.util.stream.StreamSupport;
import org.springframework.stereotype.Service;

import be.vinci.ipl.chattycar.gateway.data.*;

import java.time.LocalDate;
import java.util.*;
import org.springframework.web.server.ResponseStatusException;

@Service
public class GatewayService {

    private final AuthenticationProxy authenticationProxy;
    private final UsersProxy usersProxy;
    private final TripProxy tripProxy;
    private final NotificationsProxy notificationsProxy;
    private final PassengersProxy passengersProxy;
    private final PositionProxy positionProxy;

    public GatewayService(AuthenticationProxy authenticationProxy, UsersProxy usersProxy,
                          TripProxy tripProxy, NotificationsProxy notificationsProxy,
                          PassengersProxy passengersProxy, PositionProxy positionProxy) {
        this.authenticationProxy = authenticationProxy;
        this.usersProxy = usersProxy;
        this.tripProxy = tripProxy;
        this.notificationsProxy = notificationsProxy;
        this.passengersProxy = passengersProxy;
        this.positionProxy = positionProxy;
    }

    public String connect(Credentials credentials) {
        return authenticationProxy.connect(credentials);
    }

    public String verify(String token) {
        return authenticationProxy.verify(token);
    }

    public User createOneUser(NewUser user){
        authenticationProxy.createCredentials(user.getEmail(), user.toCredentials());
        return usersProxy.createOne(user);
    }

    public User findOneUser(String email) {
        return usersProxy.readOneByEmail(email);
    }

    public void updateOneUserPassword(Credentials credentials) {
        User user = findOneUser(credentials.getEmail());
        authenticationProxy.updateCredentials(user.getEmail(), credentials);
    }

    public User getOneUserInfo(int id) {
        return usersProxy.readOneById(id);
    }

    public User getOneUserInfo(String email) {
        return usersProxy.readOneByEmail(email);
    }

    public void updateOneUserInfo(int id, User user) {
        usersProxy.updateOne(id, user);
    }

    public void deleteOneUser(int id, String userEmail) {
        Iterable<Trip> driverTrips = tripProxy.readAllTripsByDriver(id);

        // REMOVE NOTIF OF THIS USER
        try {
            notificationsProxy.removeNotificationOfAUser(id);
        } catch (ResponseStatusException ex) {
            if (ex.getStatus().value() != 404) // Only NOT FOUND exception will be catched
                throw ex;
        }
        // REMOVE NOTIF OTHERS RECEIVE FROM THIS DRIVER
        try {
            for (Trip trip: driverTrips) {
                notificationsProxy.removeNotificationOfATrip(trip.getId());
            }
        } catch (ResponseStatusException ex) {
            if (ex.getStatus().value() != 404) // Only NOT FOUND exception will be catched
                throw ex;
        }
        // REMOVE PASSENGERS OF THIS DRIVER
        try {
            for (Trip trip: driverTrips){
                passengersProxy.deleteAllPassengersOfTrip(trip.getId());
            }
        } catch (ResponseStatusException ex) {
            if (ex.getStatus().value() != 404) // Only NOT FOUND exception will be catched
                throw ex;
        }
        // REMOVE TRIPS OF THIS DRIVER
        try {
            tripProxy.deleteAllTripsByDriver(id);
        } catch (ResponseStatusException ex) {
            if (ex.getStatus().value() != 404) // Only NOT FOUND exception will be catched
                throw ex;
        }
        // REMOVE THIS PASSENGER FROM TRIPS
        try {
            passengersProxy.deleteAllTripsFromUserWhereUserIsPassenger(id);
        } catch (ResponseStatusException ex) {
            if (ex.getStatus().value() != 404) // Only NOT FOUND exception will be catched
                throw ex;
        }

        // REMOVE THIS USER
        usersProxy.deleteOne(id);

        // REMOVE AUTH
        authenticationProxy.deleteCredentials(userEmail);
    }

    public Iterable<Trip> getAllDriverTrips(int id) {
        Iterable<Trip> trips = tripProxy.readAllTripsByDriver(id);
        System.out.println(trips);
        return StreamSupport.stream(trips.spliterator(), false)
            .filter(trip -> trip.getDepartureDate().isAfter(LocalDate.now())).toList();
    }

    public Map<String, Iterable<Trip>> getAllPassengerTrips(int userId) {
        Iterable<Trip> trips = passengersProxy.getTripsWhereUserIsPassenger(userId);
        System.out.println("trips:"+trips);
        Iterable<Trip> filteredTripsFutureDepartureDate =
            StreamSupport.stream(trips.spliterator(), false)
                .filter(trip -> trip.getDepartureDate().isAfter(LocalDate.now()))
                .toList();
        System.out.println("filtered:"+filteredTripsFutureDepartureDate);
        Map<String, Iterable<Trip>> passengerStatus = new HashMap<>();

        List<Trip> accepted = new ArrayList<>();
        List<Trip> refused = new ArrayList<>();
        List<Trip> pending = new ArrayList<>();

        for(Trip trip : filteredTripsFutureDepartureDate){
            System.out.println("trip:"+trip);
            NoIdPassenger p = passengersProxy.getOnePassenger(trip.getId(), userId);
            if (p.getStatus().equalsIgnoreCase(PassengerStatus.ACCEPTED.toString()))
                accepted.add(trip);
            else if (p.getStatus().equalsIgnoreCase(PassengerStatus.REFUSED.toString()))
                refused.add(trip);
            else if (p.getStatus().equalsIgnoreCase(PassengerStatus.PENDING.toString()))
                pending.add(trip);
        }
        passengerStatus.put(PassengerStatus.ACCEPTED.toString().toLowerCase(), accepted);
        passengerStatus.put(PassengerStatus.REFUSED.toString().toLowerCase(), refused);
        passengerStatus.put(PassengerStatus.PENDING.toString().toLowerCase(), pending);

        return passengerStatus;
    }

    public Iterable<Notification> getAllNotifs(int id) {
        return notificationsProxy.getNotification(id);
    }

    public void deleteAllNotifs(int id) {
        notificationsProxy.removeNotificationOfAUser(id);
    }

    public Trip createOneTrip(NewTrip trip) {
        return tripProxy.createOne(trip);
    }

    public Iterable<Trip> searchAllTrips(LocalDate dDate, Double oLat, Double oLon, Double dLat, Double dLon) {
        //return tripProxy.readAll(dDate, oLat, oLon, dLat, dLon);
        if (dDate != null) {
            Iterable<Trip> trips = tripProxy.readAll(dDate, null, null, null, null);
            return filterTripWithSeatingLeft(trips);
        }
        if (oLat != null || oLon != null) {
            Iterable<Trip> trips = tripProxy.readAll(null, oLat, oLon, null, null);
            Iterable<Trip> tripsFiltered = filterTripWithSeatingLeft(trips);
            return this.searchAllTripsByOriginPosition(oLat, oLon, tripsFiltered);
        }

        Iterable<Trip> trips = tripProxy.readAll(null, null, null, dLat, dLon);
        Iterable<Trip> tripsFiltered = filterTripWithSeatingLeft(trips);
        return this.searchAllTripsByDestinationPosition(dLat, dLon, tripsFiltered);
    }

    private Iterable<Trip> filterTripWithSeatingLeft(Iterable<Trip> trips) {
        return StreamSupport.stream(trips.spliterator(), false)
            .filter(trip -> {
                // Filter out trips where no available seating left
                long count = StreamSupport.stream(
                        passengersProxy.getPassengersOfTrip(trip.getId()).spliterator(), false)
                    .filter(passenger ->
                        passenger.getStatus().equalsIgnoreCase(
                            PassengerStatus.ACCEPTED.toString().toLowerCase()))
                    .count();
                System.out.println("count:"+count);
                return count < trip.getAvailableSeating();
            })
            .limit(20)
            .toList();
    }

    private Iterable<Trip> searchAllTripsByOriginPosition(Double originLat, Double originLon, Iterable<Trip> filteredTrips) {
        // Sort based on distance between originLat & originLon from user to all available trips
        return StreamSupport.stream(filteredTrips.spliterator(), false)
            .sorted(new Comparator<Trip>() {
                @Override
                public int compare(Trip o1, Trip o2) {

                    double trip1Dist = positionProxy.getDistance(originLat, originLon,
                        o1.getOrigin().getLatitude(), o1.getOrigin().getLongitude());
                    double trip2Dist = positionProxy.getDistance(originLat, originLon,
                        o2.getOrigin().getLatitude(), o2.getOrigin().getLongitude());

                    if (trip1Dist < trip2Dist) return -1;
                    else if (trip1Dist > trip2Dist) return 1;
                    return 0;
                }
            }).toList();
    }

    private Iterable<Trip> searchAllTripsByDestinationPosition(Double destinationLat, Double destinationLon,
        Iterable<Trip> filteredTrips) {
        // Sort based on distance between destinationLat & destinationLon from user to all available trips
        return StreamSupport.stream(filteredTrips.spliterator(), false)
            .sorted(new Comparator<Trip>() {
                @Override
                public int compare(Trip o1, Trip o2) {

                    double trip1Dist = positionProxy.getDistance(destinationLat, destinationLon,
                        o1.getDestination().getLatitude(), o1.getDestination().getLongitude());
                    double trip2Dist = positionProxy.getDistance(destinationLat, destinationLon,
                        o2.getDestination().getLatitude(), o2.getDestination().getLongitude());

                    if (trip1Dist < trip2Dist) return -1;
                    else if (trip1Dist > trip2Dist) return 1;
                    return 0;
                }
            }).toList();
    }

    public Trip getOneTripInformations(int tripId) {
        return tripProxy.readOne(tripId);
    }

    public void deleteOneTrip(int tripId) {
        //DELETE NOTIFICATIONS
        try {
            notificationsProxy.removeNotificationOfATrip(tripId);
        } catch (ResponseStatusException ignored) {} // Not found ignored

        //DELETE PASSENGERS
        passengersProxy.deleteAllPassengersOfTrip(tripId);
        // TODO should have a try catch. deleteAllPassengersOfTrip should send a no found error
        // TODO istead of an empty array !!

        //DELETE TRIP
        tripProxy.deleteOne(tripId);
    }

    public Map<String, Iterable<User>> getAllPassengersStatus(int tripId) {
        Iterable<Passenger> passengers = passengersProxy.getPassengersOfTrip(tripId);
        Map<String, Iterable<User>> usersTripStatus = new HashMap<>();

        List<User> accepted = new ArrayList<>();
        List<User> refused = new ArrayList<>();
        List<User> pending = new ArrayList<>();

        for(Passenger p : passengers){
            User user = usersProxy.readOneById(p.getUserId());
            System.out.println(p);
            if (p.getStatus().toUpperCase().equals(PassengerStatus.ACCEPTED.toString())){
                accepted.add(user);
            }
            else if (p.getStatus().toUpperCase().equals(PassengerStatus.REFUSED.toString())){
                refused.add(user);
            }
            else if (p.getStatus().toUpperCase().equals(PassengerStatus.PENDING.toString())){
                pending.add(user);
            } else {
                // Status not in accepted values [accepted, refused, pending]
                return null;
            }


        }
        usersTripStatus.put(PassengerStatus.ACCEPTED.toString().toLowerCase(), accepted);
        usersTripStatus.put(PassengerStatus.REFUSED.toString().toLowerCase(), refused);
        usersTripStatus.put(PassengerStatus.PENDING.toString().toLowerCase(), pending);

        return usersTripStatus;
    }

    public NoIdPassenger addOnePassenger(Trip trip, int userId) {
        long count = StreamSupport.stream(passengersProxy.getPassengersOfTrip(trip.getId()).spliterator(), false)
            .filter(passenger -> passenger.getStatus().equalsIgnoreCase(
                    PassengerStatus.ACCEPTED.toString().toLowerCase()))
            .count();
        if (count >= trip.getAvailableSeating())
            return null;

        NoIdPassenger passenger = null;
        try {
            passenger = passengersProxy.createOnePassenger(trip.getId(), userId);
        } catch (ResponseStatusException ex) {
            if (ex.getStatus().value() == 403)
                return null;
        }

        notificationsProxy.createNotification(new NoIdNotification(trip.getDriverId(), trip.getId()));
        return passenger;
    }

    public String getOnePassengerStatus(int tripId, int userId) {
        return passengersProxy.getOnePassenger(tripId, userId).getStatus();
    }

    public void updateOnePassengerStatus(int tripId, int userId, String newStatus) {
        NoIdPassenger passenger = passengersProxy.getOnePassenger(tripId, userId);
        passenger.setStatus(newStatus);
        passengersProxy.updateOnePassenger(tripId, userId, passenger);

        notificationsProxy.createNotification(new NoIdNotification(userId, tripId)); // add notif
    }

    public void deleteOnePassenger(int tripId, int userId) {
        // REMOVE NOTIF
        try {
            notificationsProxy.removeNotificationOfTripOfUser(tripId, userId);
        } catch (ResponseStatusException ignore) {} // ignore not found

        // REMOVE PASSENGER
        passengersProxy.deleteOnePassenger(tripId, userId);
    }

    public NoIdPassenger getPassenger(int tripId, int userId) {
        try {
            return passengersProxy.getOnePassenger(tripId, userId);
        } catch (ResponseStatusException ex) {
            if (ex.getStatus().value() == 404)
                return null;
        }
        return null;
    }
}
