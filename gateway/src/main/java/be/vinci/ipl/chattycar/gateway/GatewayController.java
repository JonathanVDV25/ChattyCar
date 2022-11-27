package be.vinci.ipl.chattycar.gateway;

import be.vinci.ipl.chattycar.gateway.models.Credentials;
import be.vinci.ipl.chattycar.gateway.models.User;
import be.vinci.ipl.chattycar.gateway.models.UserWithCredentials;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;


import javax.ws.rs.PathParam;

@CrossOrigin(origins = { "http://localhost" })
@RestController
public class GatewayController {

    private final GatewayService service;

    public GatewayController(GatewayService service) {
        this.service = service;
    }


    @PostMapping("/auth") //connect user and retrieve auth JWT token
    String connect(@RequestBody Credentials credentials) {
        System.out.println("/auth de gatewayController:"+credentials);
        return service.connect(credentials);
    }


    @PostMapping("/users") // create a new user
    ResponseEntity<User> createOneUser(@RequestBody UserWithCredentials user) {
        // TODO requestBody must be NewUser !!!
        return new ResponseEntity<>(service.createOneUser(user), HttpStatus.CREATED);
    }
    @GetMapping("/users") //find user from email ex: /user?email=tom.aubry@gmail.com
    ResponseEntity<User> findOneUser(@PathParam("email") String email){
        return new ResponseEntity<>(service.getOneUser(email),HttpStatus.OK);

    }
    @PutMapping("/users") //update user password
    void updateOneUser(@RequestBody Credentials credentials, @RequestHeader("Authorization") String token) {
        // TODO WHY CREDENTIALS ? MUST BE INSECURECREDENTIALS !!!!! -> ASKING TO TEACHER
        if(token==null) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        String emailToken = service.verify(token);
        if(!emailToken.equals(credentials.getEmail())) throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        service.updateOneUser(credentials);
    }

    @GetMapping("/users/{id}") //get user info
    User getOneUserInfo(@RequestHeader("Authorization") String token, @PathVariable int id) {
        // any user -> only verify if service.verify(token) != null
        if(service.verify(token) == null) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        return service.getOneUser(id);
    }

    @PutMapping("/users/{id}") // update user info
    void updateOneUserInfo(@RequestBody User user, @RequestHeader("Authorization") String token,
                            @PathVariable int id) {
        if (user.getId() != id) throw new ResponseStatusException(HttpStatus.BAD_REQUEST); //400

        String emailToken = service.verify(token);
        System.out.println(emailToken);
        if (emailToken == null) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED); // 401

        // if user.getEmail is a new email -> need to verify on userId !!
        User userFound = service.getOneUser(emailToken);
        if (userFound.getId() != user.getId()) throw new ResponseStatusException(HttpStatus.FORBIDDEN); // 403

        service.updateOneUser(id, user); // 400, 404 || 200

        // Update credentials in case email has changed
        if (!userFound.getEmail().equals(user.getEmail())) {
            service.updateOneUserEmailCred(userFound.getEmail(), user.getEmail()); // 400, 404
        }
    }
    @DeleteMapping("/users/{id}") //delete user
    void deleteOneUser(){
        //TODO
    }

    @GetMapping("/users/{id}/driver") //get trips where user is driver (departure in future)
    void getAllDriverTrips(){
        //TODO
    }
    @GetMapping("/users/{id}/passenger") //get trips where user is passenger (departure in future)
    void getAllPassengerTrips(){
        //TODO
    }
    @GetMapping("/users/{id}/notifications") //get user notifs
    void getAllNotifs(){
        //TODO
    }
    @DeleteMapping("/users/{id}/notifications") //delete all notif from user
    void deleteAllNotifs(){
        //TODO
    }

    @PostMapping("/trips") //create a trip
    void createOneTrip(){
        //TODO
    }
    @GetMapping("/trips") //get list of trip with optional search queries
    void searchAllTrips(){
        //TODO
    }
    @GetMapping("/trips/{trip_id}") //get trip informations
    void getOneTripInformations(){
        //TODO
    }
    @DeleteMapping("/trips/{trip_id}") //delete trip
    void deleteOneTrip(){
        //TODO
    }

    @GetMapping("/trips/{trip_id}/passengers") //get list of passenger of a trip (with status)
    void getAllPassengersStatus(){
        //TODO
    }

    @PostMapping("/trips/{trip_id}/passengers/{user_id}") // add passenger to trip (with pending status)
    void addOnePassenger(){
        //TODO
    }
    @GetMapping("/trips/{trip_id}/passengers/{user_id}") // get passenger status
    void getOnePassengerStatus(){
        //TODO
    }
    @PutMapping("/trips/{trip_id}/passengers/{user_id}") // update passenger status
    void updateOnePassengerStatus(){
        //TODO
    }
    @DeleteMapping("/trips/{trip_id}/passengers/{user_id}") // remove passenger from trip
    void deleteOnePassenger(){
        //TODO
    }
}
