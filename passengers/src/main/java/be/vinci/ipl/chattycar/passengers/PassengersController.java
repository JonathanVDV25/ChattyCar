package be.vinci.ipl.chattycar.passengers;

import org.springframework.stereotype.Controller;

@Controller
public class PassengersController {

  private final PassengersService service;

  public PassengersController(PassengersService service) {
    this.service = service;
  }


}
