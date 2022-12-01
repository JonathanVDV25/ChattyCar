package be.vinci.ipl.chattycar.gateway;

import be.vinci.ipl.chattycar.gateway.models.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
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

    // ok
    @GetMapping("/users/{id}/driver") //get trips where user is driver (departure in future)
    Iterable<Trip> getAllDriverTrips(@PathVariable int id, @RequestHeader("Authorization") String token){
        String email = service.verify(token);
        User user = service.getOneUserInfo(id);
//        if (!email.equals(user.getEmail()))
//            throw new ResponseStatusException(HttpStatus.FORBIDDEN); //403
        return service.getAllDriverTrips(id);
    }

    // ok
    @GetMapping("/users/{id}/passenger") //get trips where user is passenger (departure in future)
    Iterable<Trip> getAllPassengerTrips(@PathVariable int id, @RequestHeader("Authorization") String token){
        String email = service.verify(token);
        User user = service.getOneUserInfo(id);
//        if (!email.equals(user.getEmail()))
//            throw new ResponseStatusException(HttpStatus.FORBIDDEN); //403
        return service.getALlTripsUser(id);
    }

    // return code 401 & 403 missing
    @GetMapping("/users/{id}/notifications") //get user notifs -- pas testé
    void getAllNotifs(@PathVariable int id, @RequestHeader("Authorization") String token){
        System.out.println("in");
        String email = service.verify(token);
        User user = service.getOneUserInfo(id);
//        if (!email.equals(user.getEmail()))
//            throw new ResponseStatusException(HttpStatus.FORBIDDEN); //403
        service.getAllNotifs(id);
    }

    // return code 401 & 403 missing
    @DeleteMapping("/users/{id}/notifications") //delete all notif from user -- pas testé
    void deleteAllNotifs(@PathVariable int id, @RequestHeader("Authorization") String token){
        String email = service.verify(token);
        User user = service.getOneUserInfo(id);
//        if (!email.equals(user.getEmail()))
//            throw new ResponseStatusException(HttpStatus.FORBIDDEN); //403
        service.deleteAllNotifs(id);
    }

    // OK
    @PostMapping("/trips") //create a trip
    Trip createOneTrip(@RequestBody NewTrip trip, @RequestHeader("Authorization") String token){
        String email = service.verify(token);
        return service.createOneTrip(trip);
    }

    // OK
    @GetMapping("/trips") //get list of trip with optional search queries
    Iterable<Trip> searchAllTrips(@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate depDate,
                        @RequestParam(required = false) Double oLat, @RequestParam(required = false) Double oLon,
                        @RequestParam(required = false) Double dLat, @RequestParam(required = false) Double dLon) {

        return service.searchAllTrips(depDate, oLat, oLon, dLat, dLon);
    }

    // OK
    @GetMapping("/trips/{trip_id}") //get trip informations
    void getOneTripInformations(@PathVariable int trip_id){

        service.getOneTripInformations(trip_id);
    }

    // return code 401 & 403 missing -- pas testé
    @DeleteMapping("/trips/{tripId}") //delete trip
    void deleteOneTrip(@PathVariable int tripId, @RequestHeader("Authorization") String token){
        System.out.println("in");
        String email = service.verify(token);
        Trip trip = service.getOneTripInformations(tripId);
        User user = service.findOneUser(email);
        if (trip.getDriverId()!=user.getId())
            throw new ResponseStatusException(HttpStatus.FORBIDDEN); //403
        service.deleteOneTrip(tripId);
    }

    // OK mais il faut verifier la verification
    @GetMapping("/trips/{tripId}/passengers") //get list of passenger of a trip (with status)
   Map<PassengerStatus, List<Passenger>> getAllPassengersStatus(@PathVariable int tripId){//, @RequestHeader("Authorization") String token){
//        System.out.println("in");
//        String email = service.verify(token);
//        System.out.println(token);
        Trip trip = service.getOneTripInformations(tripId);
        User user = service.getOneUserInfo(trip.getDriverId());
//        if (!email.equals(user.getEmail()))
//            throw new ResponseStatusException(HttpStatus.FORBIDDEN); //403
        return service.getAllPassengersStatus(tripId);

    }

    // OK
    @PostMapping("/trips/{trip_id}/passengers/{user_id}") // add passenger to trip (with pending status)
    void addOnePassenger(@PathVariable int trip_id, @PathVariable int user_id, @RequestHeader("Authorization") String token){
        String email = service.verify(token);
        Trip trip = service.getOneTripInformations(trip_id);
        if (trip.getDriverId()==user_id)
            throw new ResponseStatusException(HttpStatus.FORBIDDEN); //403
        service.addOnePassenger(trip_id, user_id); // TODO No return value expected but service returns NoIdPasseneger
    }

    // ok
    @GetMapping("/trips/{trip_id}/passengers/{user_id}") // get passenger status
    String getOnePassengerStatus(@PathVariable int trip_id, @PathVariable int user_id, @RequestHeader("Authorization") String token){
        String email = service.verify(token);
        Trip trip = service.getOneTripInformations(trip_id);
        User user = service.getOneUserInfo(trip.getDriverId());
//        if (!email.equals(user.getEmail()))
//            throw new ResponseStatusException(HttpStatus.FORBIDDEN); //403
        return service.getOnePassengerStatus(trip_id, user_id);
    }

    // ok
    @PutMapping("/trips/{trip_id}/passengers/{user_id}") // update passenger status
    void updateOnePassengerStatus(@PathVariable int trip_id, @PathVariable int user_id,
                                  @RequestBody NoIdPassenger passenger, @RequestHeader("Authorization") String token){
        String email = service.verify(token);
        System.out.println(passenger);
        Trip trip = service.getOneTripInformations(trip_id);
        User user = service.getOneUserInfo(trip.getDriverId());
        if (trip.getDriverId()!=user.getId())
            throw new ResponseStatusException(HttpStatus.FORBIDDEN); //403
        service.updateOnePassengerStatus(trip_id, user_id, passenger);
    }

    // ok
    @DeleteMapping("/trips/{trip_id}/passengers/{user_id}") // remove passenger from trip
    void deleteOnePassenger(@PathVariable int trip_id, @PathVariable int user_id, @RequestHeader("Authorization") String token){
        String email = service.verify(token);
        Trip trip = service.getOneTripInformations(trip_id);
        User user = service.getOneUserInfo(trip.getDriverId());
        if (trip.getDriverId()!=user.getId())
            throw new ResponseStatusException(HttpStatus.FORBIDDEN); //403
        service.deleteOnePassenger(trip_id, user_id);
    }
}
