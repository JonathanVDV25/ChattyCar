package be.vinci.ipl.chattycar.gateway.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class NoIdUser {
  private String email;
  private String firstname;
  private String lastname;

  public User toUser() {
    return new User(1111, email, firstname, lastname);
  }
}
