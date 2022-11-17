package be.vinci.ipl.chattycar.passengers;

import be.vinci.ipl.chattycar.passengers.data.PassengersRepository;
import org.springframework.stereotype.Service;

@Service
public class PassengersService {

  private final PassengersRepository repository;

  public PassengersService(PassengersRepository repository) {
    this.repository = repository;
  }
}
