package be.vinci.ipl.chattycar.passengers;

import be.vinci.ipl.chattycar.passengers.models.Passenger;
import be.vinci.ipl.chattycar.passengers.models.Trip;
import be.vinci.ipl.chattycar.passengers.models.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class PassengersController {

  private final PassengersService service;

  public PassengersController(PassengersService service) {
    this.service = service;
  }

  @GetMapping("/passengers/users/{user_id}")
  public Iterable<Trip> getTripsWhereUserIsPassenger(@PathVariable int user_id) {
    return service.getTripsWhereUserIsPassenger(user_id);
  }

  @DeleteMapping("/passengers/users/{user_id}")
  public void deleteAllTripsFromUserWhereUserIsPassenger(@PathVariable int user_id) {
    service.deleteAllTripsFromUserWhereUserIsPassenger(user_id);
  }

  @GetMapping("/passengers/{trip_id}")
  public Iterable<User> getPassengersOfTrip(@PathVariable int trip_id) {
    return service.getPassengersOfTrip(trip_id);
  }

  @DeleteMapping("/passengers/{trip_id}")
  public void deleteAllPassengersOfTrip(@PathVariable int trip_id) {
    service.deleteAllPassengersOfTrip(trip_id);
  }

}
