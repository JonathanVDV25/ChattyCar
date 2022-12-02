package be.vinci.ipl.chattychar.trips;

import be.vinci.ipl.chattychar.trips.models.NewTrip;
import be.vinci.ipl.chattychar.trips.models.Trip;
import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class TripController {
  private final TripService service;

  public TripController(TripService service) {
    this.service = service;
  }

  @GetMapping("/trips")
  public Iterable<Trip> readAll(@RequestParam(required = false) @DateTimeFormat(iso = ISO.DATE) LocalDate departureDate,
                                @RequestParam(required = false) Double originLat,
                                @RequestParam(required = false) Double originLon,
                                @RequestParam(required = false) Double destinationLat,
                                @RequestParam(required = false) Double destinationLon) {

    if (departureDate != null) return service.readByDepartureDate(departureDate);
    if ((originLat != null && originLon == null) || (originLat == null && originLon != null))
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    if ((destinationLat != null && destinationLon == null) || (destinationLat == null && destinationLon != null))
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    // origin and destination return all trips
    return service.readAll();
  }

  @PostMapping("/trips")
  public ResponseEntity<Trip> createOne(@RequestBody NewTrip trip) {
    if (trip.getDepartureDate() == null || trip.getOrigin() == null ||
      trip.getDestination() == null || trip.getDriverId() < 0 ||
      trip.getAvailableSeating() < 0) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }
    return new ResponseEntity<>(service.createOneTrip(trip), HttpStatus.CREATED);
  }

  @GetMapping("/trips/{id}")
  public Trip readOne(@PathVariable int id) {
    Trip trip = service.readOneTrip(id);
    if (trip == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    return trip;
  }

  @DeleteMapping("/trips/{id}")
  public void deleteOne(@PathVariable int id) {
    boolean found = service.deleteOne(id);
    if (!found) throw new ResponseStatusException(HttpStatus.NOT_FOUND);
  }
  
  @GetMapping("/trips/user/{driver_id}")
  public Iterable<Trip> readAllTripsByDriver(@PathVariable(name = "driver_id") int driverId) {
    // Vérification si driver existe -> Gateway
    return service.readByDriver(driverId);
  }

  @DeleteMapping("/trips/user/{driver_id}")
  public void deleteAllTripsByDriver(@PathVariable(name = "driver_id") int driverId) {
    // Vérification si driver existe -> Gateway
    service.deleteAllByDriver(driverId);
  }
}
