package be.vinci.ipl.chattychar.trips.data;

import be.vinci.ipl.chattychar.trips.models.Position;
import be.vinci.ipl.chattychar.trips.models.Trip;
import java.time.LocalDate;
import javax.persistence.Tuple;
import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TripRepository extends CrudRepository<Trip, Integer> {


  Iterable<Trip> findByDepartureDate(LocalDate departureDate);

  Iterable<Trip> findByOrigin(Position origin);

  Iterable<Trip> findByDestination(Position destination);

  Iterable<Trip> findByDriverId(int driverId);

  @Transactional
  void deleteAllByDriverId(int driverId);
}
