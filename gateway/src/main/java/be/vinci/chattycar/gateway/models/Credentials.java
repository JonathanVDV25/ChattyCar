package be.vinci.chattycar.gateway.models;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Credentials {
    private String email;
    private String password;
}
