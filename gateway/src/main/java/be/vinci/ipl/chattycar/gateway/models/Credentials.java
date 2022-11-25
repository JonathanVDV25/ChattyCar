package be.vinci.ipl.chattycar.gateway.models;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Credentials {
    private String email;
    private String password;
}
