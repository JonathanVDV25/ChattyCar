package be.vinci.ipl.chattycar.users.models;

import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity(name = "users")
public class User {
  @Id
  private int id;
  private String email;
  private String firstname;
  private String lastname;
}
