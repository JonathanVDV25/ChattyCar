package be.vinci.ipl.chattycar.gateway.models;

import java.time.LocalDate;
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
public class NewTrip {
  private Position origin;
  private Position destination;
  private LocalDate departureDate;
  private int driverId;
  private int availableSeating;

  public Trip toTrip() {
    return new Trip(1111, origin, destination, departureDate, driverId, availableSeating);
  }
}
