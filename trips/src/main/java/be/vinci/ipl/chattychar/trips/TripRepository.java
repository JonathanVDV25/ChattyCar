package be.vinci.ipl.chattychar.trips;

import be.vinci.ipl.chattychar.trips.models.Trip;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TripRepository extends CrudRepository<Trip, Integer> {

}
