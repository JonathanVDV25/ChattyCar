package be.vinci.ipl.chattycar.notification;


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
public class Notification {

  private int id;
  private int userId;
  private int tripId;

  public Notification(int id_user, int id_trip){
    this.userId = id_user;
    this.tripId = id_trip;
  }
}