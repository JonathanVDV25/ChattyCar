package be.vinci.ipl.chattycar.gateway.data;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

import javax.management.Notification;

@Repository
@FeignClient(name = "notifications")
public interface NotificationsProxy {

    @PostMapping("/notifications")
    void createNotification(@RequestBody Notification notification);

    @GetMapping("/notifications/{id_user}")
    Iterable<Notification> getNotification(@PathVariable int id_user);

    @DeleteMapping("/notifications/users/{id_user}")
    void removeNotificationOfAUser(@PathVariable int id_user);

    @DeleteMapping("/notifications/trips/{id_trip}")
    void removeNotificationOfATrip(@PathVariable int id_trip);
}
