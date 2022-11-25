package be.vinci.ipl.chattycar.gateway.models;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class NoIdUser {
  private String email;
  private String firstname;
  private String lastname;

  public User toUser() {
    return new User(1111, email, firstname, lastname);
  }
}
