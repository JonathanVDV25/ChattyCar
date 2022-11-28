package be.vinci.ipl.chattycar.passengers.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Trip {

  private int id;
  private Position origin;
  private Position destination;
  @JsonProperty("departure_date")
  @Column(name = "departure_date")
  private LocalDate departureDate;
  @JsonProperty("driver_id")
  @Column(name = "driver_id")
  private int driverId;
  @JsonProperty("available_seating")
  @Column(name = "available_seating")
  private int availableSeating;

}
