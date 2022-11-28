package be.vinci.ipl.chattycar.passengers.models;

import com.fasterxml.jackson.annotation.JsonProperty;
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
public class NoIdPassenger {

  @JsonProperty("user_id")
  @Column(name = "user_id")
  private int userId;
  @JsonProperty("trip_id")
  @Column(name = "trip_id")
  private int tripId;
  private String status;
}
