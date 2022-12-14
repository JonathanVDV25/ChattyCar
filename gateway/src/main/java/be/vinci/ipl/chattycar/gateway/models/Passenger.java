package be.vinci.ipl.chattycar.gateway.models;

import com.fasterxml.jackson.annotation.JsonProperty;
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
public class Passenger {

  private int id;
  @JsonProperty("user_id")
  private int userId;
  @JsonProperty("trip_id")
  private int tripId;
  private String status;

  public Passenger(int userId, int tripId) {
    this.userId = userId;
    this.tripId = tripId;
    status = PassengerStatus.PENDING.toString().toLowerCase();
  }

  public NoIdPassenger toNoIdPassenger() {
    return new NoIdPassenger(userId, tripId, status);
  }
}
