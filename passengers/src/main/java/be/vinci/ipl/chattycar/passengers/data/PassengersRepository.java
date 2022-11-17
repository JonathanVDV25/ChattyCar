package be.vinci.ipl.chattycar.passengers.data;

import be.vinci.ipl.chattycar.passengers.models.Passenger;
import org.springframework.data.repository.CrudRepository;

public interface PassengersRepository extends CrudRepository<Passenger, Integer> {
  Iterable<Passenger> getByTripId(int tripId);

  Iterable<Passenger> getByUserId(int userId);

  Passenger findByUserIdAndTripId(int userId, int tripId);

  boolean existsByUserIdAndTripId(int userId, int tripId);
}
