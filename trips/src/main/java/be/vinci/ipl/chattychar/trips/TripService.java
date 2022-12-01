package be.vinci.ipl.chattychar.trips;

import be.vinci.ipl.chattychar.trips.data.TripRepository;
import be.vinci.ipl.chattychar.trips.models.NewTrip;
import be.vinci.ipl.chattychar.trips.models.Position;
import be.vinci.ipl.chattychar.trips.models.Trip;
import java.time.LocalDate;
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

  /**
   * Reads all Trips
   * @return The list of all trips
   */
  public Iterable<Trip> readAll() {
    return repository.findAll();
  }

  /**
   * Reads all trips of a departure date. Ordered by last created.
   * @param departureDate the departure date to search for
   * @return the list of all corresponding trips
   */
  public Iterable<Trip> readByDepartureDate(LocalDate departureDate) {
    return repository.findByDepartureDateOrderByIdDesc(departureDate);
  }

  /**
   * Reads all trips of an originLatitude and an originLongitude
   * @param originLatitude the latitude of the origin trip
   * @param originLongitude the longitude of the origin trip
   * @return the list of all corresponding trips
   */
  public Iterable<Trip> readByOriginPosition(double originLatitude, double originLongitude) {
    return repository.findByOrigin(new Position(originLatitude, originLongitude));
  }

  /**
   * Reads all trips of a destination latitude and a destination longitude
   * @param destLatitude the latitude of the destination trip
   * @param destLongitude the longitude of the destination trip
   * @return the list of all corresponding trips
   */
  public Iterable<Trip> readByDestinationPosition(double destLatitude, double destLongitude) {
    return repository.findByDestination(new Position(destLatitude, destLongitude));
  }

  /**
   * Reads all trips of a driver
   * @param driverId the ID of the driver to search for
   * @return the list of all corresponding trips
   */
  public Iterable<Trip> readByDriver(int driverId) {
    return repository.findByDriverId(driverId);
  }

  /**
   * Delete all trips of a given driver
   * @param driverId the ID of the driver to search for
   */
  public void deleteAllByDriver(int driverId) {
    repository.deleteAllByDriverId(driverId);
  }

}
