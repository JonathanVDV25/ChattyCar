package be.vinci.ipl.chattycar.users;

import be.vinci.ipl.chattycar.users.data.UsersRepository;
import be.vinci.ipl.chattycar.users.models.User;
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
  public User createOne(User user) {
    if (repository.existsById(user.getId())) return null;
    return repository.save(user);
  }

  /**
   * Reads a user
   * @param id Id of the user
   * @return The user found, or null if the user couldn't be found
   */
  public User readOneById(int id) {
    return repository.findById(id).orElse(null);
  }

  /**
   * Reads a user
   * @param email Email of the user
   * @return The user found, or null if the user couldn't be found
   */
  public User readOneByEmail(String email) {
    return repository.findByEmail(email);
  }

  /**
   * Updates a user
   * @param user User to update
   * @return True if the user could be updated, false if the user couldn't be found
   */
  public boolean updateOne(User user) {
    System.out.println(user);
    if (!repository.existsById(user.getId())) return false;
    repository.save(user);
    return true;
  }

}
