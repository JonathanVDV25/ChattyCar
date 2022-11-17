package be.vinci.ipl.chattycar.users;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class UsersController {
  private final UsersService service;

  public UsersController(UsersService service) {
    this.service = service;
  }

  @PostMapping("/users")
  public ResponseEntity<Void> createOne(@RequestBody User user) {
    if (user.getEmail() == null || user.getLastname() == null || user.getFirstname() == null) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }
    boolean created = service.createOne(user);
    if (!created) throw new ResponseStatusException(HttpStatus.CONFLICT);
    return new ResponseEntity<>(HttpStatus.CREATED);
  }

  @GetMapping("/users/{id}")
  public User readOneById(@PathVariable int id) {
    User user = service.readOneById(id);
    if (user == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    return user;
  }

  @PutMapping("/users/{id}")
  public void updateOne(@PathVariable int id, @RequestBody User user) {
    if (user.getId() != id || user.getEmail() == null ||
        user.getLastname() == null || user.getFirstname() == null) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }
    boolean found = service.updateOne(user);
    if (!found) throw new ResponseStatusException(HttpStatus.NOT_FOUND);
  }

  @GetMapping("/users/{email}")
  public User readOneByEmail(@PathVariable String email) {
    User user = service.readOneByEmail(email);
    if (user == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    return user;
  }
}
