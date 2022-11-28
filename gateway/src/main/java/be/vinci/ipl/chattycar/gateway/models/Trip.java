package be.vinci.ipl.chattycar.gateway.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;
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
//  @Embedded
//  @AttributeOverrides({
//      @AttributeOverride(name = "latitude", column = @Column(name = "origin_latitude")),
//      @AttributeOverride(name = "longitude", column = @Column(name = "origin_longitude"))
//  })
  private Position origin;

//  @Embedded
//  @AttributeOverrides({
//      @AttributeOverride(name = "latitude", column = @Column(name = "destination_latitude")),
//      @AttributeOverride(name = "longitude", column = @Column(name = "destination_longitude"))
//  })
  private Position destination;
  @JsonProperty("departure_date")
  private LocalDate departureDate;
  @JsonProperty("driver_id")
  private int driverId;
  @JsonProperty("available_seating")
  private int availableSeating;

}
