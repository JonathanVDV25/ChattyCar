package be.vinci.ipl.chattychar.trips;

import be.vinci.ipl.chattychar.trips.data.PositionProxy;
import be.vinci.ipl.chattychar.trips.data.TripRepository;
import be.vinci.ipl.chattychar.trips.models.NewTrip;
import be.vinci.ipl.chattychar.trips.models.Trip;
import java.time.LocalDate;
import java.util.stream.StreamSupport;
import org.springframework.stereotype.Service;

@Service
public class TripService {

  private final TripRepository repository;

  private final PositionProxy positionProxy;

  public TripService(TripRepository repository, PositionProxy positionProxy) {
    this.repository = repository;
    this.positionProxy = positionProxy;
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
   * Read all trips order by distance of origin position
   * @param oLat the origin latitude
   * @param oLon the origin longitude
   * @return list of trips ordered by distance from oLat & oLon
   */
  public Iterable<Trip> readAllByOrigin(double oLat, double oLon) {

    Iterable<Trip> trips = this.readAll();

    return StreamSupport.stream(trips.spliterator(), false)
        .sorted((o1, o2) -> {

          double trip1Dist = positionProxy.getDistance(oLat, oLon,
              o1.getOrigin().getLatitude(), o1.getOrigin().getLongitude());
          double trip2Dist = positionProxy.getDistance(oLat, oLon,
              o2.getOrigin().getLatitude(), o2.getOrigin().getLongitude());

          if (trip1Dist < trip2Dist) return -1;
          else if (trip1Dist > trip2Dist) return 1;
          return 0;
        }).toList();
  }

  /**
   * Read all trips order by distance of destination position
   * @param dLat the destination latitude
   * @param dLon the destination longitude
   * @return list of trips ordered by distnace from dLat & dLon
   */
  public Iterable<Trip> readAllByDestination(double dLat, double dLon) {
    Iterable<Trip> trips = this.readAll();

    return StreamSupport.stream(trips.spliterator(), false)
        .sorted((o1, o2) -> {

          double trip1Dist = positionProxy.getDistance(dLat, dLon,
              o1.getDestination().getLatitude(), o1.getDestination().getLongitude());
          double trip2Dist = positionProxy.getDistance(dLat, dLon,
              o2.getDestination().getLatitude(), o2.getDestination().getLongitude());

          if (trip1Dist < trip2Dist) return -1;
          else if (trip1Dist > trip2Dist) return 1;
          return 0;
        }).toList();
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
