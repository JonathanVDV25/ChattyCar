package be.vinci.chattycar.gateway.data;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

import be.vinci.chattycar.gateway.models.Credentials;

@Repository
@FeignClient(name = "auth")
public interface AuthenticationProxy {

    @PostMapping("/auth/connect")
    String connect(@RequestBody Credentials credentials);

    @PostMapping("/auth/verify")
    String verify(@RequestBody String token);

    @PostMapping("/auth/{email}")
    void createCredentials(@PathVariable String email, @RequestBody Credentials credentials);

    @PutMapping("/auth/{email}")
    void updateCredentials(@PathVariable String email, @RequestBody Credentials credentials);

    @DeleteMapping("/auth/{email}")
    void deleteCredentials(@PathVariable String email);

}
