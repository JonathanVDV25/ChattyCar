package be.vinci.ipl.chattycar.passengers.data;

import be.vinci.ipl.chattycar.passengers.models.Passenger;
import javax.transaction.Transactional;
import org.springframework.data.repository.CrudRepository;

public interface PassengersRepository extends CrudRepository<Passenger, Integer> {
  Iterable<Passenger> findByTripId(int tripId);

  Iterable<Passenger> findByUserId(int userId);

  Passenger findByUserIdAndTripId(int userId, int tripId);

  @Transactional
  void deleteAllByUserId(int userId);

  @Transactional
  void deleteAllByTripId(int tripId);
}
