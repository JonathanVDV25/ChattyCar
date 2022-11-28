package be.vinci.ipl.chattycar.gateway;

import be.vinci.ipl.chattycar.gateway.models.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.management.Notification;
import javax.ws.rs.PathParam;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = { "http://localhost" })
@RestController
public class GatewayController {

    // TODO check return values
    // TODO check @RequestBody, @PathVariable, @RequestHeader
    // TODO check to use verify(token)
    private final GatewayService service;

    public GatewayController(GatewayService service) {
        this.service = service;
    }


    @PostMapping("/auth") //connect user and retrieve auth JWT token
    String connect(@RequestBody Credentials credentials) {
        return service.connect(credentials);
    }


    @PostMapping("/users") // create a new user
    ResponseEntity<Void> createOneUser(@RequestBody NewUser user) {
        service.createOneUser(user);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
    @GetMapping("/users") //find user from email ex: /user?email=tom.aubry@gmail.com
    User findOneUser(@RequestHeader("email") String email){
        return service.findOneUser(email);
    }
    @PutMapping("/users") //update user password
    void updateOneUserPassword(@RequestBody Credentials user){
        service.updateOneUserPassword(user);
    }

    @GetMapping("/users/{id}") //get user info
    User getOneUserInfo(@PathVariable int id){
        return service.getOneUserInfo(id);
    }
    @PutMapping("/users/{id}") // update user info
    void updateOneUserInfo(@PathVariable int id, @RequestBody User user){
        service.updateOneUserInfo(id, user);
    }
    @DeleteMapping("/users/{id}") //delete user
    void deleteOneUser(@PathVariable int id){
        service.deleteOneUser(id);
    }

    @GetMapping("/users/{id}/driver") //get trips where user is driver (departure in future)
    Iterable<Trip> getAllDriverTrips(@PathVariable int id){
        return service.getAllDriverTrips(id);
    }
    @GetMapping("/users/{id}/passenger") //get trips where user is passenger (departure in future)
    Map<PassengerStatus, List<Trip>> getAllPassengerTrips(@PathVariable int id){
        return service.getAllPassengerTrips(id);
    }
    @GetMapping("/users/{id}/notifications") //get user notifs
    Iterable<Notification> getAllNotifs(@PathVariable int id){
        return service.getAllNotifs(id);
    }
    @DeleteMapping("/users/{id}/notifications") //delete all notif from user
    void deleteAllNotifs(@PathVariable int id){
        service.deleteAllNotifs(id);
    }

    @PostMapping("/trips") //create a trip
    Trip createOneTrip(@RequestBody NewTrip trip){
        return service.createOneTrip(trip);
    }
    @GetMapping("/trips") //get list of trip with optional search queries
    Iterable<Trip> searchAllTrips(@RequestHeader("departure_date") String depDate,
                        @RequestHeader("originLat") double oLat, @RequestHeader("originLon") double oLon,
                        @RequestHeader("destinationLat") double dLat, @RequestHeader("destinationLon") double dLon){
        LocalDate dDate = LocalDate.parse(depDate); //TODO verif
        return service.searchAllTrips(dDate, oLat, oLon, dLat, dLon);
    }
    @GetMapping("/trips/{trip_id}") //get trip informations
    Trip getOneTripInformations(@PathVariable int tripId){
        return service.getOneTripInformations(tripId);
    }
    @DeleteMapping("/trips/{trip_id}") //delete trip
    void deleteOneTrip(@PathVariable int tripId){
        service.deleteOneTrip(tripId);
    }

    @GetMapping("/trips/{id}/passengers") //get list of passenger of a trip (with status)
    Iterable<Passenger> getAllPassengersStatus(@PathVariable int tripId){
        return service.getAllPassengersStatus(tripId);
    }

    @PostMapping("/trips/{trip_id}/passengers/{user_id}") // add passenger to trip (with pending status)
    NoIdPassenger addOnePassenger(@PathVariable int tripId, @PathVariable int userId){
        return service.addOnePassenger(tripId, userId);
    }
    @GetMapping("/trips/{trip_id}/passengers/{user_id}") // get passenger status
    String getOnePassengerStatus(@PathVariable int tripId, @PathVariable int userId){
        return service.getOnePassengerStatus(tripId, userId);
    }
    @PutMapping("/trips/{trip_id}/passengers/{user_id}") // update passenger status
    void updateOnePassengerStatus(@PathVariable int tripId, @PathVariable int userId, @RequestBody NoIdPassenger passenger){
        service.updateOnePassengerStatus(tripId, userId, passenger);
    }
    @DeleteMapping("/trips/{trip_id}/passengers/{user_id}") // remove passenger from trip
    void deleteOnePassenger(@PathVariable int tripId, @PathVariable int userId){
        service.deleteOnePassenger(tripId, userId);
    }
}
