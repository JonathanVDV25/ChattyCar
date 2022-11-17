package be.vinci.ipl.chattycar.passengers;

import be.vinci.ipl.chattycar.passengers.models.NoIdPassenger;
import be.vinci.ipl.chattycar.passengers.models.PassengerStatus;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class PassengersController {

  private final PassengersService service;

  public PassengersController(PassengersService service) {
    this.service = service;
  }

  @PostMapping("/passengers/{trip_id}/{user_id}")
  public ResponseEntity<NoIdPassenger> createOnePassenger(@PathVariable(name = "trip_id") Integer tripId,
                                                      @PathVariable(name = "user_id") Integer userId) {

    NoIdPassenger passengerDB = service.getOne(tripId, userId);
    if (passengerDB != null) {
      throw new ResponseStatusException(HttpStatus.CONFLICT);
    }

    return new ResponseEntity<>(service.createOne(userId, tripId), HttpStatus.CREATED);
  }

  @GetMapping("/passengers/{trip_id}/{user_id}")
  public NoIdPassenger getOnePassenger(@PathVariable(name = "trip_id") Integer tripId,
                                  @PathVariable(name = "user_id") Integer userId) {

    NoIdPassenger passenger = service.getOne(tripId, userId);
    if (passenger == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    return passenger;
  }

  @PutMapping("/passengers/{trip_id}/{user_id}")
  public void updateOnePassenger(@PathVariable(name = "trip_id") int tripId,
                                @PathVariable(name = "user_id") int userId,
                                @RequestBody NoIdPassenger passenger) {
    if (passenger == null || passenger.getTripId() != tripId || passenger.getUserId() != userId ||
        passenger.getStatus() == null) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }
    if (passenger.getStatus().equalsIgnoreCase(PassengerStatus.ACCEPTED.toString()) &&
        passenger.getStatus().equalsIgnoreCase(PassengerStatus.PENDING.toString()) &&
        passenger.getStatus().equalsIgnoreCase(PassengerStatus.REFUSED.toString())) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }

    // TODO No verification if passenger.status is the same as the one in DB !
    boolean updatedPassenger = service.updateOne(userId, tripId, passenger);
    if (!updatedPassenger) throw new ResponseStatusException(HttpStatus.NOT_FOUND);

  }

  @DeleteMapping("/passengers/{trip_id}/{user_id}")
  public void deleteOnePassenger(@PathVariable(name = "trip_id") int tripId,
                                @PathVariable(name = "user_id") int userId) {
    boolean deletedPassenger = service.deleteOne(userId, tripId);
    if (!deletedPassenger) throw new ResponseStatusException(HttpStatus.NOT_FOUND);
  }


}
