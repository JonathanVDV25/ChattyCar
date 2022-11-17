package be.vinci.ipl.chattycar.position;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class Position {
  private int id;
  private double latitude;
  private double longitude;

}
