package be.vinci.ipl.chattycar.gateway.models;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UserWithCredentials {

    private String email;
    private String firstname;
    private String lastname;
    private String password;

    public NoIdUser toNoIdUser() {
        return new NoIdUser(email, firstname, lastname);
    }
    public Credentials toCredentials() {
        return new Credentials(email, password);
    }
}