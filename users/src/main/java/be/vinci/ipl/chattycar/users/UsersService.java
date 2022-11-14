package be.vinci.ipl.chattycar.users;

import org.springframework.stereotype.Service;

@Service
public class UsersService {
  private final UsersRepository repository;

  public UsersService(UsersRepository repository) {
    this.repository = repository;
  }

  /**
   * Creates a user
   * @param user User to create
   * @return true if the user could be created, false if another user exists with this id
   */
  public boolean createOne(User user) {
    if (repository.existsById(user.getId())) return false;
    repository.save(user);
    return true;
  }

  /**
   * Reads a user
   * @param id Id of the user
   * @return The user found, or null if the user couldn't be found
   */
  public User readOne(int id) {
    return repository.findById(id).orElse(null);
  }

}
