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

  @GetMapping("/distance/{origin_latitude}/{origin_longitude}/{destination_latitude}/{destination_longitude}")
  public double getDistance(@PathVariable("origin_latitude") double originLatitude,
      @PathVariable("origin_longitude") double originLongitude,
      @PathVariable("destination_latitude") double destinationLatitude,
      @PathVariable("destination_longitude") double destinationLongitude) {
    if (originLatitude == 0 || originLongitude == 0 || destinationLatitude == 0 || destinationLongitude == 0) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }
    return service.getDistance(originLatitude, originLongitude, destinationLatitude, destinationLongitude);
  }
}
