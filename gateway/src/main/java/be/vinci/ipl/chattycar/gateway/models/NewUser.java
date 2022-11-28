package be.vinci.ipl.chattycar.gateway.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class NewUser {
  private String email;
  private String firstname;
  private String lastname;
  private String password;

  public User toUser() {
    return new User(0, email, firstname, lastname);
  }
  public Credentials toCredentials(){ return new Credentials(email, password);
  }
}
