package be.vinci.ipl.chattycar.gateway.data;

import be.vinci.ipl.chattycar.gateway.models.Credentials;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

@Repository
@FeignClient(name = "authentication")
public interface AuthenticationProxy {

    @PostMapping("/authentication/connect")
    String connect(@RequestBody Credentials credentials);

    @GetMapping("/authentication/{email}")
    Credentials getOne(@PathVariable String email);

    @PostMapping("/authentication/verify")
    String verify(@RequestBody String token);

    @PostMapping("/authentication/{email}")
    void createCredentials(@PathVariable String email, @RequestBody Credentials credentials);

    @PutMapping("/authentication/{email}")
    void updateCredentials(@PathVariable String email, @RequestBody Credentials credentials);

    @PutMapping("/authentication/email/{oldEmail}")
    void updateOneOnlyEmail(@PathVariable String oldEmail, @RequestBody String newEmail);

    @DeleteMapping("/authentication/{email}")
    void deleteCredentials(@PathVariable String email);

}