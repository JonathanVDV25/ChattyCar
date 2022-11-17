package be.vinci.ipl.chattycar.notification;


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
@Entity(name = "notifications")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int userId;
    private int tripId;

    public Notification(int id_user, int id_trip){
        this.id = 0;
        this.userId = id_user;
        this.tripId = id_trip;
    }
}
