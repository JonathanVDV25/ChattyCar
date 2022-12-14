package be.vinci.ipl.chattycar.gateway;

import be.vinci.ipl.chattycar.gateway.models.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = { "http://localhost:3000/", "http://localhost" })
@RestController
public class GatewayController {

    private final GatewayService service;

    public GatewayController(GatewayService service) {
        this.service = service;
    }


    // ok
    @PostMapping("/auth") //connect user and retrieve auth JWT token
    String connect(@RequestBody Credentials credentials) {
        return service.connect(credentials);
    }

    // ok
    @PostMapping("/users") // create a new user
    User createOneUser(@RequestBody NewUser user) {
        User newUser = service.createOneUser(user);
        if(newUser==null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        return newUser  ;
    }

    // ok
    @GetMapping("/users") //find user from email ex: /user?email=tom.aubry@gmail.com
    User findOneUser(@RequestParam String email){
        return service.findOneUser(email);
    }

    // ok
    @PutMapping("/users") //update user password
    void updateOneUserPassword(@RequestBody Credentials user, @RequestHeader("Authorization") String token){
        String email = service.verify(token);
        if (!email.equals(user.getEmail()))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN); //403
        service.updateOneUserPassword(user);
    }


    @GetMapping("/users/{id}") //get user info
    User getOneUserInfo(@PathVariable int id, @RequestHeader("Authorization") String token){
        service.verify(token);
        return service.getOneUserInfo(id);
    }

    @PutMapping("/users/{id}") // update user info
    void updateOneUserInfo(@PathVariable int id, @RequestBody User user, @RequestHeader("Authorization") String token){
        String email = service.verify(token);
        if (!email.equals(user.getEmail()))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN); //403
        service.updateOneUserInfo(id, user);
    }

    @DeleteMapping("/users/{id}") //delete user
    void deleteOneUser(@PathVariable int id, @RequestHeader("Authorization") String token){
        String email = service.verify(token); // 401
        User user = service.getOneUserInfo(id); // 404
        if (!email.equals(user.getEmail()))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN); //403
        service.deleteOneUser(id, email);
    }

    @GetMapping("/users/{id}/driver") //get trips where user is driver (departure in future)
    Iterable<Trip> getAllDriverTrips(@PathVariable int id, @RequestHeader("Authorization") String token){
        String email = service.verify(token);   // 401
        User user = service.getOneUserInfo(id); // 404
        if (!email.equals(user.getEmail()))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN); //403
        return service.getAllDriverTrips(id);
    }

    @GetMapping("/users/{id}/passenger") //get trips where user is passenger (departure in future)
    Map<String, Iterable<Trip>> getAllPassengerTrips(@PathVariable int id,
        @RequestHeader("Authorization") String token){
        String email = service.verify(token);
        User user = service.getOneUserInfo(id);
        if (!email.equals(user.getEmail()))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN); //403
        return service.getAllPassengerTrips(id);
    }

    // return code 401 & 403 missing
    @GetMapping("/users/{id}/notifications")
    Iterable<Notification> getAllNotifs(@PathVariable int id, @RequestHeader("Authorization") String token){
        String email = service.verify(token); // 401
        User user = service.getOneUserInfo(id); // 404
        if (!email.equals(user.getEmail()))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN); //403
        return service.getAllNotifs(id);
    }

    @DeleteMapping("/users/{id}/notifications") //delete all notif from user -- pas test??
    void deleteAllNotifs(@PathVariable int id, @RequestHeader("Authorization") String token){
        String email = service.verify(token);   // 401
        User user = service.getOneUserInfo(id); // 404
        if (!email.equals(user.getEmail()))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN); //403
        service.deleteAllNotifs(id);
    }

    @PostMapping("/trips") //create a trip
    Trip createOneTrip(@RequestBody NewTrip trip, @RequestHeader("Authorization") String token){
        String email = service.verify(token);  // 401
        User user = service.getOneUserInfo(email);
        if (trip.getDriverId() != user.getId()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN); // 403
        }
        return service.createOneTrip(trip); // 400 || 201
    }


    @GetMapping("/trips") //get list of trip with optional search queries
    Iterable<Trip> searchAllTrips(
        @RequestParam(required = false, name = "departure_date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate depDate,
            @RequestParam(required = false, name = "originLat") Double oLat,
            @RequestParam(required = false, name = "originLon") Double oLon,
            @RequestParam(required = false, name = "destinationLat") Double dLat,
            @RequestParam(required = false, name = "destinationLon") Double dLon) {

        return service.searchAllTrips(depDate, oLat, oLon, dLat, dLon); // 400 || 200
    }

    @GetMapping("/trips/{trip_id}") //get trip informations
    Trip getOneTripInformations(@PathVariable int trip_id){
        return service.getOneTripInformations(trip_id);
    }

    @DeleteMapping("/trips/{tripId}") //delete trip
    void deleteOneTrip(@PathVariable int tripId, @RequestHeader("Authorization") String token) {
        String email = service.verify(token); // 401
        Trip trip = service.getOneTripInformations(tripId); // 404
        User user = service.findOneUser(email); // never 404
        if (trip.getDriverId()!=user.getId())
            throw new ResponseStatusException(HttpStatus.FORBIDDEN); //403
        service.deleteOneTrip(tripId);
    }

    @GetMapping("/trips/{id}/passengers") //get list of passenger of a trip (with status)
    Map<String, Iterable<User>> getAllPassengersStatus(@PathVariable int id, @RequestHeader("Authorization") String token){
        String email = service.verify(token); // 401
        Trip trip = service.getOneTripInformations(id); // 404
        User user = service.getOneUserInfo(trip.getDriverId());
        if (!email.equals(user.getEmail()))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN); //403
        Map<String, Iterable<User>> result = service.getAllPassengersStatus(id);
        if (result == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST); //400
        return result;
    }

    @PostMapping("/trips/{trip_id}/passengers/{user_id}") // add passenger to trip (with pending status)
    ResponseEntity<NoIdPassenger> addOnePassenger(@PathVariable("trip_id") int tripId, @PathVariable("user_id") int userId,
        @RequestHeader("Authorization") String token){
        String email = service.verify(token); //401

        Trip trip = service.getOneTripInformations(tripId); //404
        User user = service.getOneUserInfo(userId); // 404

        if (!email.equals(user.getEmail()))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN); //403

        NoIdPassenger passenger = service.addOnePassenger(trip, userId);
        if (passenger == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST); // 400
        return new ResponseEntity<>(passenger, HttpStatus.CREATED);
    }

    @GetMapping("/trips/{trip_id}/passengers/{user_id}") // get passenger status
    String getOnePassengerStatus(@PathVariable int trip_id, @PathVariable int user_id, @RequestHeader("Authorization") String token){
        String email = service.verify(token); //401
        User userEmail = service.getOneUserInfo(email);

        Trip trip = service.getOneTripInformations(trip_id); //404
        User userDriver = service.getOneUserInfo(trip.getDriverId()); //404

        // See if he is passenger of trip
        NoIdPassenger p = service.getPassenger(trip_id, userEmail.getId());
        if (!email.equals(userDriver.getEmail()) && p == null)
            throw new ResponseStatusException(HttpStatus.FORBIDDEN); //403
        return service.getOnePassengerStatus(trip_id, user_id);
    }

    @PutMapping("/trips/{trip_id}/passengers/{user_id}") // update passenger status
    void updateOnePassengerStatus(@PathVariable(name = "trip_id") int tripId,
        @PathVariable(name = "user_id") int userId,
        @RequestParam(required = true, name = "status") String newStatus,
        @RequestHeader("Authorization") String token) {

        String email = service.verify(token); // 401
        Trip trip = service.getOneTripInformations(tripId); //404
        User userDriver = service.getOneUserInfo(trip.getDriverId());

        if (!email.equals(userDriver.getEmail()))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN); //403
        service.updateOnePassengerStatus(tripId, userId, newStatus);
    }

    @DeleteMapping("/trips/{trip_id}/passengers/{user_id}") // remove passenger from trip
    void deleteOnePassenger(@PathVariable(name = "trip_id") int tripId, @PathVariable(name = "user_id") int userId,
        @RequestHeader("Authorization") String token) {
        String email = service.verify(token); // 401
        User user = service.getOneUserInfo(userId); // 404
        service.getOneTripInformations(tripId); // 404
        try {
            service.getPassenger(tripId, userId);
        } catch (ResponseStatusException ex) {
            if (ex.getStatus().value() == 404)
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        if (!email.equals(user.getEmail()))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN); //403
        service.deleteOnePassenger(tripId, userId);
    }
}
