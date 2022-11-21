package be.vinci.chattycar.gateway.models;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private String id;
    private String email;
    private String firstname;
    private String lastname;
}
