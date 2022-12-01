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
@NoArgsConstructor
@AllArgsConstructor
public class NoIdNotification {
  @JsonProperty("user_id")
  private int userId;
  @JsonProperty("trip_id")
  private int tripId;

  public Notification toNotification() {
    return new Notification(1111, userId, tripId);
  }
}
