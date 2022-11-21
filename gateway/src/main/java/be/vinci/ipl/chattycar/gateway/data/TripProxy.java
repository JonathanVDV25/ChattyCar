package be.vinci.ipl.chattycar.gateway.data;

import be.vinci.ipl.chattycar.gateway.models.Trip;
import be.vinci.ipl.chattycar.gateway.models.NewTrip;

import java.time.LocalDate;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Repository
@FeignClient(name = "trips")
public interface TripProxy {

  @GetMapping("/trips")
  Iterable<Trip> readAll(@RequestParam(required = false) @DateTimeFormat(iso = ISO.DATE) LocalDate departureDate,
                         @RequestParam(required = false) Double originLat,
                         @RequestParam(required = false) Double originLon,
                         @RequestParam(required = false) Double destinationLat,
                         @RequestParam(required = false) Double destinationLon);

  @PostMapping("/trips")
  Trip createOne(@RequestBody NewTrip trip);

  @GetMapping("/trips/{id}")
  Trip readOne(@PathVariable int id);

  @DeleteMapping("/trips/{id}")
  void deleteOne(@PathVariable int id);

  @GetMapping("/trips/user/{driver_id}")
  Iterable<Trip> readAllTripsByDriver(@PathVariable int driver_id);

  @DeleteMapping("/trips/user/{driver_id}")
  void deleteAllTripsByDriver(@PathVariable int driver_id);
}
