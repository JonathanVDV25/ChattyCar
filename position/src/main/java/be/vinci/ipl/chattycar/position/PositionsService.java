package be.vinci.ipl.chattycar.position;

import org.springframework.stereotype.Service;

@Service
public class PositionsService {

  private final int EARTH_RADIUS = 6371;

  /**
   * Calculates distance between two geographical positions
   * @param originLatitude Latitude of the origin position
   * @param originLongitude Longitude of the origin position
   * @param destinationLatitude Latitude of the destination position
   * @param destinationLongitude Longitude of the destination position
   * @return the calculated distance
   */
  public double getDistance(double originLatitude, double originLongitude,
      double destinationLatitude, double destinationLongitude) {
    double distance = Math.acos(Math.sin(destinationLatitude * Math.PI / 180.0) * Math.sin(originLatitude * Math.PI / 180.0) +
        Math.cos(destinationLatitude * Math.PI / 180.0) * Math.cos(originLatitude * Math.PI / 180.0) *
            Math.cos((originLongitude - destinationLongitude) * Math.PI / 180.0)) * EARTH_RADIUS;
    return distance;
  }
}
