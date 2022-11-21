package be.vinci.ipl.chattycar.gateway.data;

import be.vinci.ipl.chattycar.gateway.models.NoIdUser;
import be.vinci.ipl.chattycar.gateway.models.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Repository
@FeignClient(name = "users")
public interface UsersProxy {
  @PostMapping("/users")
  User createOne(@RequestBody NoIdUser noIdUser);

  @GetMapping("/users/{id}")
  User readOneById(@PathVariable int id);

  @PutMapping("/users/{id}")
  void updateOne(@PathVariable int id, @RequestBody User user);

  @GetMapping("/users/email/{email}")
  User readOneByEmail(@PathVariable String email);
}
