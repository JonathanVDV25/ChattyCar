package be.vinci.ipl.chattycar.gateway.data;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Repository
@FeignClient(name = "position")
public interface PositionProxy {
  @GetMapping("/distance/{originLatitude}/{originLongitude}/{destinationLatitude}/{destinationLongitude}")
  double getDistance(@PathVariable double originLatitude, @PathVariable double originLongitude,
      @PathVariable double destinationLatitude, @PathVariable double destinationLongitude);
}
