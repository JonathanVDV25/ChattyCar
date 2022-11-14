package be.vinci.ipl.chattychar.trips;

import be.vinci.ipl.chattychar.trips.models.NewTrip;
import be.vinci.ipl.chattychar.trips.models.Trip;
import org.springframework.stereotype.Service;

@Service
public class TripService {

  private final TripRepository repository;

  public TripService(TripRepository repository) {
    this.repository = repository;
  }

  /**
   * Creates a new trip
   * @param newTrip the trip to create
   * @return the trip of the trip created
   */
  public Trip createOneTrip(NewTrip newTrip) {
    return repository.save(newTrip.toTrip());
  }

  /**
   * Reads a trip
   * @param id ID of the trip
   * @return the trip found, or null if none was found
   */
  public Trip readOneTrip(int id) {
    return repository.findById(id).orElse(null);
  }

  /**
   * Deletes a trip
   * @param id ID of the trip
   * @return True if the trip could be deleted, or false if the trip couldn't be found
   */
  public boolean deleteOne(int id) {
    if (!repository.existsById(id)) return false;
    repository.deleteById(id);
    return true;
  }


}
