package be.vinci.ipl.chattycar.passengers;

import be.vinci.ipl.chattycar.passengers.data.PassengersRepository;
import be.vinci.ipl.chattycar.passengers.data.TripsProxy;
import be.vinci.ipl.chattycar.passengers.data.UsersProxy;
import be.vinci.ipl.chattycar.passengers.models.Passenger;
import be.vinci.ipl.chattycar.passengers.models.Trip;
import be.vinci.ipl.chattycar.passengers.models.User;
import java.util.stream.StreamSupport;
import org.springframework.stereotype.Service;

@Service
public class PassengersService {

  private final PassengersRepository repository;
  private final TripsProxy tripsProxy;
  private final UsersProxy usersProxy;

  public PassengersService(PassengersRepository repository, TripsProxy tripsProxy,
      UsersProxy usersProxy) {
    this.repository = repository;
    this.tripsProxy = tripsProxy;
    this.usersProxy = usersProxy;
  }

  public Iterable<Trip> getTripsWhereUserIsPassenger(int userId) {
    Iterable<Passenger> passengers = repository.getByUserId(userId);
    return StreamSupport.stream(passengers.spliterator(), false)
        .map(passenger -> tripsProxy.readTrip(passenger.getTripId()))
        .toList();
  }

  public void deleteAllTripsFromUserWhereUserIsPassenger(int userId) {
    Iterable<Passenger> passengers = repository.getByUserId(userId);
    StreamSupport.stream(passengers.spliterator(), false)
        .forEach(passenger -> repository.delete(passenger));
  }

  public Iterable<User> getPassengersOfTrip(int tripId) {
    Iterable<Passenger> passengers = repository.getByTripId(tripId);
    return StreamSupport.stream(passengers.spliterator(), false)
        .map(passenger -> usersProxy.readUser(passenger.getUserId()))
        .toList();
  }

  public void deleteAllPassengersOfTrip(int tripId) {
    Iterable<Passenger> passengers = repository.getByTripId(tripId);
    StreamSupport.stream(passengers.spliterator(), false)
        .forEach(passenger -> repository.delete(passenger));
  }
}
