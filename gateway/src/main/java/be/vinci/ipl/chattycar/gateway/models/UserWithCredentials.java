package be.vinci.ipl.chattycar.gateway.models;

import be.vinci.chattycar.gateway.models.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class UserWithCredentials {
    private String id;
    private String email;
    private String firstname;
    private String lastname;
    private String password;

    public be.vinci.chattycar.gateway.models.User toUser() {
        return new User(id, email, firstname, lastname);
    }
    public Credentials toCredentials() {
        return new Credentials(email, password);
    }
}