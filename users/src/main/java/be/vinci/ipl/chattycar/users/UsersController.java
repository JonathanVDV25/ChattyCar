package be.vinci.ipl.chattycar.users;

import be.vinci.ipl.chattycar.users.models.NoIdUser;
import be.vinci.ipl.chattycar.users.models.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
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
  public ResponseEntity<User> createOne(@RequestBody NoIdUser noIdUser) {
    if (noIdUser.getEmail() == null || noIdUser.getLastname() == null || noIdUser.getFirstname() == null) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }
    User createdUser = service.createOne(noIdUser);
    if (createdUser == null) throw new ResponseStatusException(HttpStatus.CONFLICT);
    return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
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

  @DeleteMapping("/users/{id}")
  public void deleteOne(@PathVariable int id) {
    boolean found = service.deleteOne(id);
    if (!found) throw new ResponseStatusException(HttpStatus.NOT_FOUND);
  }

  @GetMapping("/users/email/{email}")
  public User readOneByEmail(@PathVariable String email) {
    User user = service.readOneByEmail(email);
    if (user == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    return user;
  }
}
