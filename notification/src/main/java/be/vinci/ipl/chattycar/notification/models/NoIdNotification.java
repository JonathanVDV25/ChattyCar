package be.vinci.ipl.chattycar.notification.models;


import com.fasterxml.jackson.annotation.JsonProperty;
import javax.persistence.Column;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class NoIdNotification {

  private int id;
  @JsonProperty("user_id")
  private int userId;
  @JsonProperty("trip_id")
  private int tripId;

  public Notification toNotification() {
    return new Notification(1111, userId, tripId);
  }
}
