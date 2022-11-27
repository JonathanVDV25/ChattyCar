package be.vinci.ipl.chattycar.authentication;

import be.vinci.ipl.chattycar.authentication.models.Credentials;
import be.vinci.ipl.chattycar.authentication.models.InsecureCredentials;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class AuthenticationController {
    private final AuthenticationService service;

    public AuthenticationController(AuthenticationService service){
        this.service = service;
    }

    @PostMapping("/authentication/{email}")
    public ResponseEntity<Void> createOne(@PathVariable String email, @RequestBody InsecureCredentials credentials) {
        if (credentials.getEmail() == null || credentials.getPassword() == null ||
                !credentials.getEmail().equals(email)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST); //400
        }
        boolean created = service.createOne(credentials);
        if (!created) throw new ResponseStatusException(HttpStatus.CONFLICT); //409
        return new ResponseEntity<>(HttpStatus.CREATED); //201
    }

    @GetMapping("/authentication/{email}")
    public Credentials getOne(@PathVariable String email) {
        Credentials credentials = service.getOne(email);
        System.out.println("getOne credentials: "+credentials);
        if (credentials == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        return credentials;
    }

    @PutMapping("/authentication/{email}")
    public void updateOne(@PathVariable String email, @RequestBody InsecureCredentials credentials) {
        if (credentials.getEmail() == null || credentials.getPassword() == null ||
                !credentials.getEmail().equals(email)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST); //400
        }
        boolean found = service.updateOne(credentials);
        if (!found) throw new ResponseStatusException(HttpStatus.NOT_FOUND); //404
    }

    @PutMapping("/authentication/email/{oldEmail}")
    public void updateOneOnlyEmail(@PathVariable String oldEmail, @RequestBody String newEmail) {
        boolean found = service.updateOne(oldEmail, newEmail);
        System.out.println("updateOne credentials: "+found);
        if (!found) throw new ResponseStatusException(HttpStatus.NOT_FOUND); //404
    }

    @DeleteMapping("/authentication/{email}")
    public void deleteOne(@PathVariable String email) {
        boolean found = service.deleteOne(email);
        if (!found) throw new ResponseStatusException(HttpStatus.NOT_FOUND); //404
    }


    @PostMapping("/authentication/connect")
    public String connect(@RequestBody InsecureCredentials credentials) {
        System.out.println("ce qui arrive:"+credentials);
        if (credentials.getEmail() == null || credentials.getPassword() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST); //400
        }
        String token = service.connect(credentials);
        if (token == null) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED); //401
        return token;
    }


    @PostMapping("/authentication/verify")
    public String verify(@RequestBody String token) {
        String email = service.verify(token);
        if (email == null) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED); //401
        return email;
    }

}
