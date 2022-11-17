package be.vinci.ipl.chattycar.position;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class PositionsController {
  private final PositionsService service;

  public PositionsController(PositionsService service) {
    this.service = service;
  }

  @GetMapping("/distance/{originLatitude}/{originLongitude}/{destinationLatitude}/{destinationLongitude}")
  public double getDistance(@PathVariable double originLatitude, @PathVariable double originLongitude,
      @PathVariable double destinationLatitude, @PathVariable double destinationLongitude) {
    if (originLatitude == 0 || originLongitude == 0 || destinationLatitude == 0 || destinationLongitude == 0) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }
    double distance = service.getDistance(originLatitude, originLongitude, destinationLatitude, destinationLongitude);
    return distance;
  }
}
