package be.vinci.ipl.chattychar.trips.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;
import javax.persistence.Column;
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
  @JsonProperty("departure_date")
  @Column(name = "departure")
  private LocalDate departureDate;
  @JsonProperty("driver_id")
  @Column(name = "driver_id")
  private int driverId;
  @JsonProperty("available_seating")
  @Column(name = "available_seating")
  private int availableSeating;

  public Trip toTrip() {
    return new Trip(1111, origin, destination, departureDate, driverId, availableSeating);
  }
}
