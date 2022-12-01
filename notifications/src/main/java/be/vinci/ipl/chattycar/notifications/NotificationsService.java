package be.vinci.ipl.chattycar.notifications;

import be.vinci.ipl.chattycar.notifications.data.NotificationsRepository;
import be.vinci.ipl.chattycar.notifications.models.NoIdNotification;
import be.vinci.ipl.chattycar.notifications.models.Notification;
import org.springframework.stereotype.Service;

@Service
public class NotificationsService {

    private final NotificationsRepository repository;

    public NotificationsService(NotificationsRepository repository){
        this.repository = repository;
    }

    /**
     * Create a notification to a user
     * @param  noIdNotification notification to create
     * @return created notification
     */
    public Notification createOne(NoIdNotification noIdNotification) {
        if(repository.existsByUserIdAndTripId(noIdNotification.getUserId(), noIdNotification.getTripId())){
            return null;
        }
        return repository.save(noIdNotification.toNotification());
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

    public boolean deleteNotificationOfTripOfUser(int tripId, int userId) {
        if (!repository.existsByUserIdAndTripId(userId, tripId))
            return false;
        repository.deleteByTripIdAndAndUserId(tripId, userId);
        return true;
    }

}
