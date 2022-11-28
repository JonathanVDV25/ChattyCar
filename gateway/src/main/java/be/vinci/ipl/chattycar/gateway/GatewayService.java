package be.vinci.ipl.chattycar.gateway;

import be.vinci.ipl.chattycar.gateway.models.*;
import org.springframework.stereotype.Service;

import be.vinci.ipl.chattycar.gateway.data.*;

import javax.management.Notification;
import java.time.LocalDate;

@Service
public class GatewayService {

    private final AuthenticationProxy authenticationProxy;
    private final UsersProxy usersProxy;
    private final TripProxy tripProxy;
    private final NotificationProxy notificationProxy;
    private final PassengersProxy passengersProxy;

    public GatewayService(AuthenticationProxy authenticationProxy, UsersProxy usersProxy,
                          TripProxy tripProxy, NotificationProxy notificationProxy,
                          PassengersProxy passengersProxy) {
        this.authenticationProxy = authenticationProxy;
        this.usersProxy = usersProxy;
        this.tripProxy = tripProxy;
        this.notificationProxy = notificationProxy;
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
        authenticationProxy.createCredentials(user.getEmail(), credentials);
    }

    public User getOneUserInfo(int id) {
        return usersProxy.readOneById(id);
    }

    public void updateOneUserInfo(int id, User user) {
        usersProxy.updateOne(id, user);
    }

    public void deleteOneUser(int id) {
        Iterable<Trip> driverTrips = tripProxy.readAllTripsByDriver(id);

        // REMOVE NOTIF OF THIS USER
        notificationProxy.removeNotificationOfAUser(id);

        // REMOVE NOTIF OTHERS RECEIVE FROM THIS DRIVER
        for (Trip trip: driverTrips){
            notificationProxy.removeNotificationOfATrip(trip.getId());
        }

        // REMOVE PASSENGERS OF THIS DRIVER
        for (Trip trip: driverTrips){
            passengersProxy.deleteAllPassengersOfTrip(trip.getId());
        }

        // REMOVE TRIPS OF THIS DRIVER
        tripProxy.deleteAllTripsByDriver(id);

        // REMOVE THIS PASSENGER FROM TRIPS
        passengersProxy.deleteAllTripsFromUserWhereUserIsPassenger(id);

        // REMOVE THIS USER
        //usersProxy.//TODO deleteOneUser pas present dans usersProxy

        // REMOVE AUTH
        authenticationProxy.deleteCredentials(usersProxy.readOneById(id).getEmail());
    }

    public Iterable<Trip> getAllDriverTrips(int id) {
        return tripProxy.readAllTripsByDriver(id);
    }

    public Iterable<User> getAllPassengerTrips(int id) {
        return passengersProxy.getPassengersOfTrip(id);
    }

    public Iterable<Notification> getAllNotifs(int id) {
        return notificationProxy.getNotification(id);
    }

    public void deleteAllNotifs(int id) {
        notificationProxy.removeNotificationOfAUser(id);
    }

    public Trip createOneTrip(NewTrip trip) {
        return tripProxy.createOne(trip);
    }

    public Iterable<Trip> searchAllTrips(LocalDate dDate, double oLat, double oLon, double dLat, double dLon) {
        return tripProxy.readAll(dDate, oLat, oLon, dLat, dLon);
    }

    public Trip getOneTripInformations(int tripId) {
        return tripProxy.readOne(tripId);
    }

    public void deleteOneTrip(int tripId) {
        //DELETE NOTIFICATIONS
        notificationProxy.removeNotificationOfATrip(tripId);

        //DELETE PASSENGERS
        passengersProxy.deleteAllPassengersOfTrip(tripId);

        //DELETE TRIP
        tripProxy.deleteOne(tripId);
    }

    public void getAllPassengersStatus(int tripId) {
        Iterable<User> users = passengersProxy.getPassengersOfTrip(tripId);
        for (User user : users){
            user.
        }
        Iterable<String> status;
        return passengersProxy.getPassengersOfTrip()//TODO prendre uniquement status
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
}
