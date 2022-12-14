package be.vinci.ipl.chattycar.gateway.data;

import be.vinci.ipl.chattycar.gateway.models.NoIdNotification;
import be.vinci.ipl.chattycar.gateway.models.Notification;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

@Repository
@FeignClient(name = "notifications")
public interface NotificationsProxy {

    @PostMapping("/notifications")
    void createNotification(@RequestBody NoIdNotification notification);

    @GetMapping("/notifications/{id_user}")
    Iterable<Notification> getNotification(@PathVariable int id_user);

    @DeleteMapping("/notifications/users/{id_user}")
    void removeNotificationOfAUser(@PathVariable int id_user);

    @DeleteMapping("/notifications/trips/{id_trip}")
    void removeNotificationOfATrip(@PathVariable int id_trip);

    @DeleteMapping("/notifications/trips/{trip_id}/users/{user_id}")
    void removeNotificationOfTripOfUser(@PathVariable(name = "trip_id") int tripId,
        @PathVariable(name = "user_id") int userId);
}
