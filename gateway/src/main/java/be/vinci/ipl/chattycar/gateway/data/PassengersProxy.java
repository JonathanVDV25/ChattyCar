package be.vinci.ipl.chattycar.gateway.data;

import be.vinci.ipl.chattycar.gateway.models.NoIdPassenger;
import be.vinci.ipl.chattycar.gateway.models.Trip;
import be.vinci.ipl.chattycar.gateway.models.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Repository
@FeignClient(name = "passengers")
public interface PassengersProxy {
  @PostMapping("/passengers/{trip_id}/{user_id}")
  NoIdPassenger createOnePassenger(@PathVariable(name = "trip_id") Integer tripId,
                                   @PathVariable(name = "user_id") Integer userId);

  @GetMapping("/passengers/{trip_id}/{user_id}")
  NoIdPassenger getOnePassenger(@PathVariable(name = "trip_id") Integer tripId,
      @PathVariable(name = "user_id") Integer userId);

  @PutMapping("/passengers/{trip_id}/{user_id}")
  void updateOnePassenger(@PathVariable(name = "trip_id") int tripId,
      @PathVariable(name = "user_id") int userId,
      @RequestBody NoIdPassenger passenger);

  @DeleteMapping("/passengers/{trip_id}/{user_id}")
  void deleteOnePassenger(@PathVariable(name = "trip_id") int tripId,
      @PathVariable(name = "user_id") int userId);

  @GetMapping("/passengers/users/{user_id}")
  Iterable<Trip> getTripsWhereUserIsPassenger(@PathVariable int user_id);

  @DeleteMapping("/passengers/users/{user_id}")
  void deleteAllTripsFromUserWhereUserIsPassenger(@PathVariable int user_id);

  @GetMapping("/passengers/{trip_id}")
  Iterable<User> getPassengersOfTrip(@PathVariable int trip_id);

  @DeleteMapping("/passengers/{trip_id}")
  void deleteAllPassengersOfTrip(@PathVariable int trip_id);
}
