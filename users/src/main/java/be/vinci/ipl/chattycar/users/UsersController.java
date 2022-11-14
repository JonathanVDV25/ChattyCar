package be.vinci.ipl.chattycar.users;

import org.springframework.web.bind.annotation.RestController;

@RestController
public class UsersController {
  private final UsersService service;

  public UsersController(UsersService service) {
    this.service = service;
  }
}
