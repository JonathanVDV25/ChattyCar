package be.vinci.ipl.chattycar.passengers;

import be.vinci.ipl.chattycar.passengers.data.PassengersRepository;
import be.vinci.ipl.chattycar.passengers.models.NoIdPassenger;
import be.vinci.ipl.chattycar.passengers.models.Passenger;
import be.vinci.ipl.chattycar.passengers.data.TripsProxy;
import be.vinci.ipl.chattycar.passengers.models.Trip;
import java.util.stream.StreamSupport;
import org.springframework.stereotype.Service;

@Service
public class PassengersService {

  private final PassengersRepository repository;
  private final TripsProxy tripsProxy;
  public PassengersService(PassengersRepository repository, TripsProxy tripsProxy
      ) {
    this.repository = repository;
    this.tripsProxy = tripsProxy;
  }

  /**
   * Get a list of trips where the user is the passenger.
   * @param userId the id of the user.
   * @return list of trips.
   */
  public Iterable<Trip> getTripsWhereUserIsPassenger(int userId) {
    Iterable<Passenger> passengers = repository.findByUserId(userId);
    return StreamSupport.stream(passengers.spliterator(), false)
        .map(passenger -> tripsProxy.readTrip(passenger.getTripId()))
        .toList();
  }

  /**
   * Delete all trips where user is passenger.
   * @param userId the id pf user.
   */
  public void deleteAllTripsFromUserWhereUserIsPassenger(int userId) {
    repository.deleteAllByUserId(userId);
  }

  /**
   * Get a list of passengers of a trip.
   * @param tripId the id of the trip.
   * @return tripId the id of the trip
   * @return list of passenger
   */
  public Iterable<Passenger> getPassengersOfTrip(int tripId) {
    return repository.findByTripId(tripId);
  }

  /**
   * Delete all the passengers of a trip.
   * @param tripId the id of a trip.
   */
  public void deleteAllPassengersOfTrip(int tripId) {
    repository.deleteAllByTripId(tripId);
  }

  /**
   * Get a passenger of a trip.
   * @param tripId the id of a trip.
   * @param userId the id of a user.
   * @return NoIdPassenger the passenger found, else null
   */
  public NoIdPassenger getOne(int tripId, int userId) {
    Passenger p = repository.findByUserIdAndTripId(userId, tripId);
    return p == null ? null : p.toNoIdPassenger();
  }

  /**
   * Create a passenger for a trip.
   * @param userId the id of a user.
   * @param tripId the id of a trip.
   * @return the new passenger
   */
  public NoIdPassenger createOne(int userId, int tripId) {
    return repository.save(new Passenger(userId, tripId)).toNoIdPassenger();
  }

  /**
   * Update the status of a passenger.
   * @param userId the id of the user.
   * @param tripId the id of the trip.
   * @param passenger the updated passenger.
   * @return
   */
  public boolean updateOne(int userId, int tripId, NoIdPassenger passenger) {
    Passenger passengerDB = repository.findByUserIdAndTripId(userId, tripId);
    if (passengerDB == null)
      return false;

    passengerDB.setStatus(passenger.getStatus().toLowerCase()); // Always set in lowercase
    repository.save(passengerDB);
    return true;
  }

  /**
   * Delete a passenger.
   * @param userId the user id.
   * @param tripId the trip id.
   * @return true if the passenger is deleted, false if not.
   */
  public boolean deleteOne(int userId, int tripId) {
    Passenger passenger = repository.findByUserIdAndTripId(userId, tripId);
    if (passenger == null) return false;
    repository.deleteById(passenger.getId());
    return true;
  }

}
