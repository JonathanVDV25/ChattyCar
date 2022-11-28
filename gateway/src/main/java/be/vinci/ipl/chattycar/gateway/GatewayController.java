package be.vinci.ipl.chattycar.gateway;

import be.vinci.ipl.chattycar.gateway.models.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = { "http://localhost:53709/", "http://localhost" })
@RestController
public class GatewayController {
    
    private final GatewayService service;

    public GatewayController(GatewayService service) {
        this.service = service;
    }


    // return code ok
    @PostMapping("/auth") //connect user and retrieve auth JWT token
    String connect(@RequestBody Credentials credentials) {
        return service.connect(credentials);
    }

    // return code ok
    @PostMapping("/users") // create a new user
    User createOneUser(@RequestBody NewUser user) {
        return service.createOneUser(user);
    }

    // return code ok
    @GetMapping("/users") //find user from email ex: /user?email=tom.aubry@gmail.com
    User findOneUser(@RequestHeader("email") String email){
        return service.findOneUser(email);
    }

    // return code ok 401 403
    @PutMapping("/users") //update user password
    void updateOneUserPassword(@RequestBody Credentials user, @RequestHeader("Authorization") String token){
        String email = service.verify(token);
        if (!email.equals(user.getEmail()))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN); //403
        service.updateOneUserPassword(user);
    }

    // return code 401 missing
    @GetMapping("/users/{id}") //get user info
    User getOneUserInfo(@PathVariable int id, @RequestHeader("Authorization") String token){
        service.verify(token);
        User user = service.getOneUserInfo(id);
        //if (!user.getEmail().equals(email))
        //    throw new ResponseStatusException(HttpStatus.UNAUTHORIZED); //401
        return user;
    }

    // return code 401 & 403 missing
    @PutMapping("/users/{id}") // update user info
    void updateOneUserInfo(@PathVariable int id, @RequestBody User user, @RequestHeader("Authorization") String token){
        String email = service.verify(token);
        if (!email.equals(user.getEmail()))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN); //403
        service.updateOneUserInfo(id, user);
    }

    // return code 401 & 403 missing
    @DeleteMapping("/users/{id}") //delete user
    void deleteOneUser(@PathVariable int id, @RequestHeader("Authorization") String token){
        String email = service.verify(token);
        User user = service.getOneUserInfo(id);
        if (!email.equals(user.getEmail()))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN); //403
        service.deleteOneUser(id);
    }

    // return code 401 & 403 missing
    @GetMapping("/users/{id}/driver") //get trips where user is driver (departure in future)
    Iterable<Trip> getAllDriverTrips(@PathVariable int id, @RequestHeader("Authorization") String token){
        String email = service.verify(token);
        User user = service.getOneUserInfo(id);
        if (!email.equals(user.getEmail()))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN); //403
        return service.getAllDriverTrips(id);
    }

    // return code 401 & 403 missing
    @GetMapping("/users/{id}/passenger") //get trips where user is passenger (departure in future)
    void getAllPassengerTrips(@PathVariable int id, @RequestHeader("Authorization") String token){
        String email = service.verify(token);
        User user = service.getOneUserInfo(id);
        if (!email.equals(user.getEmail()))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN); //403
        service.getAllPassengerTrips(id);
    }

    // return code 401 & 403 missing
    @GetMapping("/users/{id}/notifications") //get user notifs
    void getAllNotifs(@PathVariable int id, @RequestHeader("Authorization") String token){
        String email = service.verify(token);
        User user = service.getOneUserInfo(id);
        if (!email.equals(user.getEmail()))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN); //403
        service.getAllNotifs(id);
    }

    // return code 401 & 403 missing
    @DeleteMapping("/users/{id}/notifications") //delete all notif from user
    void deleteAllNotifs(@PathVariable int id, @RequestHeader("Authorization") String token){
        String email = service.verify(token);
        User user = service.getOneUserInfo(id);
        if (!email.equals(user.getEmail()))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN); //403
        service.deleteAllNotifs(id);
    }

    // return code 401 & 403 missing
    @PostMapping("/trips") //create a trip
    void createOneTrip(@RequestBody NewTrip trip, @RequestHeader("Authorization") String token){
        String email = service.verify(token);
        User user = service.getOneUserInfo(trip.getDriverId());
        if (!email.equals(user.getEmail()))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN); //403
        service.createOneTrip(trip);
    }

    // return code ok
    @GetMapping("/trips") //get list of trip with optional search queries
    void searchAllTrips(@RequestHeader("departure_date") String depDate,
                        @RequestHeader("originLat") double oLat, @RequestHeader("originLon") double oLon,
                        @RequestHeader("destinationLat") double dLat, @RequestHeader("destinationLon") double dLon){
        LocalDate dDate = LocalDate.parse(depDate); //TODO verif
        service.searchAllTrips(dDate, oLat, oLon, dLat, dLon);
    }

    // return code ok
    @GetMapping("/trips/{trip_id}") //get trip informations
    void getOneTripInformations(@PathVariable int tripId){
        service.getOneTripInformations(tripId);
    }

    // return code 401 & 403 missing
    @DeleteMapping("/trips/{trip_id}") //delete trip
    void deleteOneTrip(@PathVariable int tripId, @RequestHeader("Authorization") String token){
        String email = service.verify(token);
        Trip trip = service.getOneTripInformations(tripId);
        User user = service.getOneUserInfo(trip.getDriverId());
        if (!email.equals(user.getEmail()))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN); //403
        service.deleteOneTrip(tripId);
    }

    // return code 401 & 403 missing
    @GetMapping("/trips/{id}/passengers") //get list of passenger of a trip (with status)
    Map<PassengerStatus, List<Passenger>> getAllPassengersStatus(@PathVariable int tripId, @RequestHeader("Authorization") String token){
        String email = service.verify(token);
        Trip trip = service.getOneTripInformations(tripId);
        User user = service.getOneUserInfo(trip.getDriverId());
        if (!email.equals(user.getEmail()))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN); //403
        return service.getAllPassengersStatus(tripId);
    }

    // return code 401 & 403 missing
    @PostMapping("/trips/{trip_id}/passengers/{user_id}") // add passenger to trip (with pending status)
    void addOnePassenger(@PathVariable int tripId, @PathVariable int userId, @RequestHeader("Authorization") String token){
        String email = service.verify(token);
        Trip trip = service.getOneTripInformations(tripId);
        User user = service.getOneUserInfo(trip.getDriverId());
        if (!email.equals(user.getEmail()))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN); //403
        service.addOnePassenger(tripId, userId); // TODO No return value expected but service returns NoIdPasseneger
    }

    // return code 401 & 403 missing
    @GetMapping("/trips/{trip_id}/passengers/{user_id}") // get passenger status
    void getOnePassengerStatus(@PathVariable int tripId, @PathVariable int userId, @RequestHeader("Authorization") String token){
        String email = service.verify(token);
        Trip trip = service.getOneTripInformations(tripId);
        User user = service.getOneUserInfo(trip.getDriverId());
        if (!email.equals(user.getEmail()))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN); //403
        service.getOnePassengerStatus(tripId, userId);
    }

    // return code 401 & 403 missing
    @PutMapping("/trips/{trip_id}/passengers/{user_id}") // update passenger status
    void updateOnePassengerStatus(@PathVariable int tripId, @PathVariable int userId,
                                  @RequestBody NoIdPassenger passenger, @RequestHeader("Authorization") String token){
        String email = service.verify(token);
        Trip trip = service.getOneTripInformations(tripId);
        User user = service.getOneUserInfo(trip.getDriverId());
        if (!email.equals(user.getEmail()))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN); //403
        service.updateOnePassengerStatus(tripId, userId, passenger);
    }

    // return code 401 & 403 missing
    @DeleteMapping("/trips/{trip_id}/passengers/{user_id}") // remove passenger from trip
    void deleteOnePassenger(@PathVariable int tripId, @PathVariable int userId, @RequestHeader("Authorization") String token){
        String email = service.verify(token);
        Trip trip = service.getOneTripInformations(tripId);
        User user = service.getOneUserInfo(trip.getDriverId());
        if (!email.equals(user.getEmail()))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN); //403
        service.deleteOnePassenger(tripId, userId);
    }
}
