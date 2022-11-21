package be.vinci.ipl.chattycar.notification;

import org.springframework.stereotype.Service;

@Service
public class NotificationsService {

    private final NotificationsRepository repository;

    public NotificationsService(NotificationsRepository repository){
        this.repository = repository;
    }

    /**
     * Create a notification to a user
     * @param  notification to create
     * @return true if the notification could be created, false if it already exists
     */
    public boolean createNotificationToUser(Notification notification) {
        if(repository.existsByUserIdAndTripId(notification.getUserId(), notification.getTripId())){
            return false;
        }
        repository.save(new Notification(notification.getUserId(), notification.getTripId()));
        return true;
    }

    /**
     * Get all notification for a User
     * @param id_user id of a User
     * @return an iterable of all the notification from a User or null
     */

    public Iterable<Notification> getAllNotificationForAUser(int id_user){
        Iterable<Notification> notifications = repository.findAllByUserId(id_user);
        return notifications;
    }

    /**
     * Delete all notification
     * @param id_user id of a User
     * @return true if all is deleted, false if not
     */

    public boolean deleteAllNotification(int id_user) {
        if(!repository.existsByUserId(id_user)){
            return false;
        }
        repository.deleteAllByUserId(id_user);
        return true;
    }

    /**
     * Delete all notification associated to a trip
     * @param id_trip
     * @return true if all is deleted, false if not
     */
    public boolean deleteNotificationOfATrip(int id_trip){
        if(!repository.existsByTripId(id_trip)){
            return false;
        }
        repository.deleteAllByTripId(id_trip);
        return true;
    }

}
