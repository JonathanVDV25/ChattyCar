package be.vinci.ipl.chattycar.gateway;

import be.vinci.ipl.chattycar.gateway.models.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import be.vinci.ipl.chattycar.gateway.data.*;

import javax.management.Notification;
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

    public GatewayService(AuthenticationProxy authenticationProxy, UsersProxy usersProxy,
                          TripProxy tripProxy, NotificationsProxy notificationsProxy,
                          PassengersProxy passengersProxy) {
        this.authenticationProxy = authenticationProxy;
        this.usersProxy = usersProxy;
        this.tripProxy = tripProxy;
        this.notificationsProxy = notificationsProxy;
        this.passengersProxy = passengersProxy;
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
        return tripProxy.readAllTripsByDriver(id);
    }

    public Iterable<Trip> getALlTripsUser(int id) {
        return passengersProxy.getTripsWhereUserIsPassenger(id);
    }
    public Map<PassengerStatus, List<Trip>> getAllPassengerTrips(int id) {
        // TODO il manque le status (comme clé)(GET /users/{id}/passenger)
        Iterable<Trip> trips = passengersProxy.getTripsWhereUserIsPassenger(id);
        Map<PassengerStatus, List<Trip>> status = new HashMap<PassengerStatus, List<Trip>>();

        List<Trip> accepted = new ArrayList<>();
        List<Trip> refused = new ArrayList<>();
        List<Trip> pending = new ArrayList<>();

        for(Trip trip : trips){
            NoIdPassenger p = passengersProxy.getOnePassenger(trip.getId(), id);
            if (p.getStatus().equals(PassengerStatus.ACCEPTED))
                accepted.add(trip);
            else if (p.getStatus().equals(PassengerStatus.REFUSED))
                refused.add(trip);
            else if (p.getStatus().equals(PassengerStatus.PENDING))
                pending.add(trip);
        }
        status.put(PassengerStatus.ACCEPTED, accepted);
        status.put(PassengerStatus.REFUSED, refused);
        status.put(PassengerStatus.PENDING, pending);

        return status;
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
        return tripProxy.readAll(dDate, oLat, oLon, dLat, dLon);
    }

    public Trip getOneTripInformations(int tripId) {
        return tripProxy.readOne(tripId);
    }

    public void deleteOneTrip(int tripId) {
        //DELETE NOTIFICATIONS
        notificationsProxy.removeNotificationOfATrip(tripId);

        //DELETE PASSENGERS
        passengersProxy.deleteAllPassengersOfTrip(tripId);

        //DELETE TRIP
        tripProxy.deleteOne(tripId);
    }

    public Map<PassengerStatus, List<Passenger>> getAllPassengersStatus(int tripId) {
        Iterable<Passenger> passengers = passengersProxy.getPassengersOfTrip(tripId);
        Map<PassengerStatus, List<Passenger>> status = new HashMap<>();

        List<Passenger> accepted = new ArrayList<>();
        List<Passenger> refused = new ArrayList<>();
        List<Passenger> pending = new ArrayList<>();

        for(Passenger p : passengers){
            System.out.println(p);
            if (p.getStatus().toUpperCase().equals(PassengerStatus.ACCEPTED.toString())){
                accepted.add(p);
            }
            else if (p.getStatus().toUpperCase().equals(PassengerStatus.REFUSED.toString())){
                refused.add(p);
            }
            else if (p.getStatus().toUpperCase().equals(PassengerStatus.PENDING.toString())){
                pending.add(p);
            }


        }
        status.put(PassengerStatus.ACCEPTED, accepted);
        status.put(PassengerStatus.REFUSED, refused);
        status.put(PassengerStatus.PENDING, pending);

        return status;
    }

    public NoIdPassenger addOnePassenger(int tripId, int userId) {
        return passengersProxy.createOnePassenger(tripId, userId);
    }

    public String getOnePassengerStatus(int tripId, int userId) {
        return passengersProxy.getOnePassenger(tripId, userId).getStatus();
    }

    public void updateOnePassengerStatus(int tripId, int userId, NoIdPassenger passenger) {
        passengersProxy.updateOnePassenger(tripId, userId, passenger);
    }

    public void deleteOnePassenger(int tripId, int userId) {
        // REMOVE PASSENGER
        passengersProxy.deleteOnePassenger(tripId, userId);
    }

    public Iterable<Passenger> listPassengers(int tripId){
        return passengersProxy.getPassengersOfTrip(tripId);
    }
}
