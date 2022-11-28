package be.vinci.ipl.chattycar.notification;

import be.vinci.ipl.chattycar.notification.models.Notification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class NotificationsController {
    private final NotificationsService service;

    public NotificationsController(NotificationsService notificationsService){
        this.service = notificationsService;
    }

    /**
     * Create a new notification to user
     * @request POST/notifications/{id_user}/{id_trip}
     * @response 409: notification already exists, 201:
     */
    @PostMapping("/notifications/{id_user}/{id_trip}")
    public ResponseEntity<Void> createNotification(@RequestBody Notification notification) {
        boolean created = service.createNotificationToUser(notification);
        if(!created) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    /**
     * Get all notification from a user
     * @request GET/notifications/{id_user}
     * @response 404: if there's no notification, 200 if there is
     */
    @GetMapping("/notifications/{id_user}")
    public Iterable<Notification> getNotification(@PathVariable int id_user){
        return service.getAllNotificationForAUser(id_user);
    }

    /**
     * Delete all notification from a user
     * @request DELETE/notifications/{id_user}
     * @response 404: if there's no notification, 200 if there is
     */
    @DeleteMapping("/notifications/{id_user}")
    public void removeNotificationOfAUser(@PathVariable int id_user){
        boolean found = service.deleteAllNotification(id_user);
        if(!found) throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    /**
     * Delete all notification associated to a trip
     * @request DELETE/notification/{id_trip}
     * @response 404: if there's no trip, 200 if there is
     */
    @DeleteMapping("/notification/{id_trip}")
    public void removeNotificationOfATrip(@PathVariable int id_trip){
        boolean found = service.deleteNotificationOfATrip(id_trip);
        if(!found) throw  new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

}
