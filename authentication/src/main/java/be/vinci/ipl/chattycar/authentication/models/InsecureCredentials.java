package be.vinci.ipl.chattycar.authentication.models;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class InsecureCredentials {
    private String email;
    private String password;

    public Credentials toCredentials(String hashedPassword) {
        return new Credentials(email, hashedPassword);
    }
}