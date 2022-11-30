package be.vinci.ipl.chattycar.notifications;

import be.vinci.ipl.chattycar.notifications.models.NoIdNotification;
import be.vinci.ipl.chattycar.notifications.models.Notification;
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

    @PostMapping("/notifications")
    public ResponseEntity<Notification> createNotification(@RequestBody NoIdNotification noIdNotification) {
        Notification notification = service.createOne(noIdNotification);
        if(notification == null) return new ResponseEntity<>(HttpStatus.CONFLICT);
        return new ResponseEntity<>(notification, HttpStatus.CREATED);
    }

    @GetMapping("/notifications/{user_id}")
    public Iterable<Notification> getNotification(@PathVariable("user_id") int userId){
        return service.getAllNotificationForAUser(userId);
    }

    @DeleteMapping("/notifications/{user_id}")
    public void removeNotificationOfAUser(@PathVariable("user_id") int userId){
        boolean found = service.deleteAllNotification(userId);
        if(!found) throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/notification/{trip_id}")
    public void removeNotificationOfATrip(@PathVariable("trip_id") int tripId){
        boolean found = service.deleteNotificationOfATrip(tripId);
        if(!found) throw  new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

}
