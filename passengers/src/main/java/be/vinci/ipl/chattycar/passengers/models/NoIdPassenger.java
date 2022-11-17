package be.vinci.ipl.chattycar.passengers.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class NoIdPassenger {

  private int userId;
  private int tripId;
  private String status;
}
