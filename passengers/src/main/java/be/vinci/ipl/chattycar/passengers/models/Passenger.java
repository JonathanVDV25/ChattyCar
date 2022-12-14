package be.vinci.ipl.chattycar.passengers.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
@Entity(name = "passengers")
public class Passenger {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;
  @JsonProperty("user_id")
  @Column(name = "user_id")
  private int userId;
  @JsonProperty("trip_id")
  @Column(name = "trip_id")
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
