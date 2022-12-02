package be.vinci.ipl.chattychar.trips.data;

import be.vinci.ipl.chattychar.trips.models.Trip;
import java.time.LocalDate;
import javax.transaction.Transactional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TripRepository extends CrudRepository<Trip, Integer> {


  Iterable<Trip> findByDepartureDateOrderByIdDesc(LocalDate departureDate);

  Iterable<Trip> findByDriverId(int driverId);

  @Transactional
  void deleteAllByDriverId(int driverId);
}
