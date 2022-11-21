package be.vinci.chattycar.gateway;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import be.vinci.chattycar.gateway.models.*;

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
        return service.connect(credentials);
    }


    @PostMapping("/users") // create a new user
    void createOneUser(@RequestBody UserWithCredentials user) {
        service.createOneUser(user);
        //TODO
    }
    @GetMapping("/users") //find user from email ex: /user?email=tom.aubry@gmail.com
    void findOneUser(@PathParam("email") String email){
        service.findOneUser();
        //TODO
    }
    @PutMapping("/users") //update user password
    void updateOneUserPassword(){
        service.findOneUser();
        //TODO
    }

    @GetMapping("/users/{id}") //get user info
    void getOneUserInfo(){
        //TODO
    }
    @PutMapping("/users/{id}") // update user info
    void updateOneUserInfo(){
        //TODO
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
