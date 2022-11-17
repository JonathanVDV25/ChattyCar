package be.vinci.ipl.chattycar.passengers;

import be.vinci.ipl.chattycar.passengers.data.PassengersRepository;
import be.vinci.ipl.chattycar.passengers.models.NoIdPassenger;
import be.vinci.ipl.chattycar.passengers.models.Passenger;
import org.springframework.stereotype.Service;

@Service
public class PassengersService {

  private final PassengersRepository repository;

  public PassengersService(PassengersRepository repository) {
    this.repository = repository;
  }

  public NoIdPassenger getOne(int tripId, int userId) {
    Passenger p = repository.findByUserIdAndTripId(userId, tripId);
    return p == null ? null : p.toNoIdPassenger();
  }

  public NoIdPassenger createOne(int userId, int tripId) {
    return repository.save(new Passenger(userId, tripId)).toNoIdPassenger();
  }

  public boolean updateOne(int userId, int tripId, NoIdPassenger passenger) {
    Passenger passengerDB = repository.findByUserIdAndTripId(userId, tripId);
    if (passengerDB == null)
      return false;

    passengerDB.setStatus(passenger.getStatus().toLowerCase()); // Always set in lowercase
    repository.save(passengerDB);
    return true;
  }

  public boolean deleteOne(int userId, int tripId) {
    Passenger passenger = repository.findByUserIdAndTripId(userId, tripId);
    if (passenger == null) return false;
    repository.deleteById(passenger.getId());
    return true;
  }

}
