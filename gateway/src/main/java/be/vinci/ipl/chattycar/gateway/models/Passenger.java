package be.vinci.ipl.chattycar.gateway.models;

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
  private int userId;
  private int tripId;
  private String status;

  public Passenger(int userId, int tripId) {
    this.userId = userId;
    this.tripId = tripId;
    status = PassengerStatus.PENDING.toString().toLowerCase();
  }

  public Passenger(int userId, int tripId, String status) {
    this.userId = userId;
    this.tripId = tripId;
    this.status = status;
  }

  public NoIdPassenger toNoIdPassenger() {
    return new NoIdPassenger(userId, tripId, status);
  }
}
