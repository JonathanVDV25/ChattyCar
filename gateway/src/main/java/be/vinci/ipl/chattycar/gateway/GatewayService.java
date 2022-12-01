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

    /**
     * Connect user.
     * @param credentials the user's credentials.
     * @return token created from the user's credentials.
     */
    public String connect(Credentials credentials) {
        return authenticationProxy.connect(credentials);
    }

    /**
     * Verify the user's token.
     * @param token the user's token.
     * @return the decoded string.
     */
    public String verify(String token) {
        return authenticationProxy.verify(token);
    }

    /**
     * Create user.
     * @param user the new user.
     * @return the new user.
     */
    public User createOneUser(NewUser user){
        authenticationProxy.createCredentials(user.getEmail(), user.toCredentials());
        return usersProxy.createOne(user);
    }

    /**
     * Find user from email.
     * @param email the user's email.
     * @return the user.
     */
    public User findOneUser(String email) {
        return usersProxy.readOneByEmail(email);
    }

    /**
     * Update the user password.
     * @param credentials the user's credentials.
     */
    public void updateOneUserPassword(Credentials credentials) {
        User user = findOneUser(credentials.getEmail());
        authenticationProxy.updateCredentials(user.getEmail(), credentials);
    }

    /**
     * Get user info from userId.
     * @param id the user's id.
     * @return the user.
     */
    public User getOneUserInfo(int id) {
        return usersProxy.readOneById(id);
    }

    /**
     * Get user info from user email.
     * @param email the email of the user.
     * @return the user.
     */
    public User getOneUserInfo(String email) {
        return usersProxy.readOneByEmail(email);
    }

    /**
     * Update user info.
     * @param id the id of user.
     * @param user the new user's info.
     */
    public void updateOneUserInfo(int id, User user) {
        usersProxy.updateOne(id, user);
    }

    /**
     * Delete user.
     * @param id the user's id.
     * @param userEmail the user's email.
     */
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

    /**
     * Get a list of trips of a driver.
     * @param id the driver id.
     * @return the list of trips.
     */
    public Iterable<Trip> getAllDriverTrips(int id) {
        Iterable<Trip> trips = tripProxy.readAllTripsByDriver(id);
        System.out.println(trips);
        return StreamSupport.stream(trips.spliterator(), false)
            .filter(trip -> trip.getDepartureDate().isAfter(LocalDate.now())).toList();
    }

    /**
     * Get a list of trips of a passenger with the status.
     * @param userId the user's id.
     * @return the list of trips.
     */
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

    /**
     * Get a list of notifications of a user.
     * @param id the user's id.
     * @return the list of notifications.
     */
    public Iterable<Notification> getAllNotifs(int id) {
        return notificationsProxy.getNotification(id);
    }

    /**
     * Delete all notifications of a user.
     * @param id the user's id.
     */
    public void deleteAllNotifs(int id) {
        notificationsProxy.removeNotificationOfAUser(id);
    }

    /**
     * Create a new trip.
     * @param trip the new trip .
     * @return the trip created.
     */
    public Trip createOneTrip(NewTrip trip) {
        return tripProxy.createOne(trip);
    }

    /**
     * Get a list of trips with the info provided.
     * @param dDate date of the trip.
     * @param oLat the origin's latitude of the trip.
     * @param oLon the origin's longitude of the trip.
     * @param dLat the destination's latitude of the trip.
     * @param dLon the destination's longitude of the trip.
     * @return list of trips.
     */
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
        if (dLat != null || dLon != null) {
            Iterable<Trip> trips = tripProxy.readAll(null, null, null, dLat, dLon);
            Iterable<Trip> tripsFiltered = filterTripWithSeatingLeft(trips);
            return this.searchAllTripsByDestinationPosition(dLat, dLon, tripsFiltered);
        }

        // return all
        Iterable<Trip> trips = tripProxy.readAll(null, null, null, null, null);
        return filterTripWithSeatingLeft(trips);
    }

    /**
     * Get list of trips with seat available.
     * @param trips the list of trips to sort.
     * @return the list of trips with seat available.
     */
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

    /**
     * Get a list of trips with the origin provided by user.
     * @param originLat the origin's latitude.
     * @param originLon the origin's longitude.
     * @param filteredTrips the sorted trips.
     * @return the list of trips sorted.
     */
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

    /**
     * Get a list of trips with the destination provided by user.
     * @param destinationLat the destination's latitude.
     * @param destinationLon the destination's longitude.
     * @param filteredTrips the sorted trips.
     * @return the list of trips sorted
     */
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

    /**
     * Get a trip's info.
     * @param tripId the trip's id.
     * @return the trip's info.
     */
    public Trip getOneTripInformations(int tripId) {
        return tripProxy.readOne(tripId);
    }

    /**
     * Delete a trip.
     * @param tripId the trip's id.
     */
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

    /**
     * Get a list of user's status of a trip.
     * @param tripId the id of a trip.
     * @return the map of user's status of a trip.
     */
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

    /**
     * Add a passenger to a trip.
     * @param trip the trip's id.
     * @param userId the user's id.
     * @return the new passenger of the trip.
     */
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

        notificationsProxy.createNotification(new NoIdNotification(trip.getDriverId(), trip.getId(), LocalDate.now(), "Membre " + userId + "veut rejoindre le voyage " + trip.getId()));
        return passenger;
    }

    /**
     * Get a status of a passenger from a trip.
     * @param tripId the trip's id.
     * @param userId the user's id.
     * @return the passenger.
     */
    public String getOnePassengerStatus(int tripId, int userId) {
        return passengersProxy.getOnePassenger(tripId, userId).getStatus();
    }

    /**
     * Update the status of a passenger.
     * @param tripId the trip's id.
     * @param userId the user's id.
     * @param newStatus the new status of the passenger.
     */
    public void updateOnePassengerStatus(int tripId, int userId, String newStatus) {
        NoIdPassenger passenger = passengersProxy.getOnePassenger(tripId, userId);
        passenger.setStatus(newStatus);
        passengersProxy.updateOnePassenger(tripId, userId, passenger);

        notificationsProxy.createNotification(new NoIdNotification(userId, tripId, LocalDate.now(), "Votre demande de voyage " + tripId + " est " + newStatus)); // add notif
    }

    /**
     * Delete a passenger of a trip.
     * @param tripId the trip's id.
     * @param userId the user's id.
     */
    public void deleteOnePassenger(int tripId, int userId) {
        // REMOVE NOTIF
        try {
            notificationsProxy.removeNotificationOfTripOfUser(tripId, userId);
        } catch (ResponseStatusException ignore) {} // ignore not found

        // REMOVE PASSENGER
        passengersProxy.deleteOnePassenger(tripId, userId);
    }

    /**
     * Get the passenger of a trip.
     * @param tripId the trip's id.
     * @param userId the user's id.
     * @return
     */
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
