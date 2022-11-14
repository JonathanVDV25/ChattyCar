package be.vinci.ipl.chattychar.trips.models;

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
  private String departure;
  private int driverId;
  private int availableSeating;

  public Trip toTrip() {
    return new Trip(1111, origin, destination, departure, driverId, availableSeating);
  }
}
