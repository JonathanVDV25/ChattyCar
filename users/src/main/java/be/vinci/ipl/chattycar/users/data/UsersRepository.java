package be.vinci.ipl.chattycar.users.data;

import be.vinci.ipl.chattycar.users.models.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersRepository extends CrudRepository<User, Integer> {

  User findByEmail(String email);
}
