package be.vinci.ipl.chattycar.notification.data;

import be.vinci.ipl.chattycar.notification.models.Notification;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface NotificationsRepository extends CrudRepository<Notification, Integer> {

    Iterable<Notification> findAllByUserId(int id_user);

    boolean existsByUserIdAndTripId(int id_user, int id_trip);

    boolean existsByUserId(int id_user);

    boolean existsByTripId(int id_trip);
    @Transactional
    void deleteAllByUserId(int id_user);

    @Transactional
    void deleteAllByTripId(int id_trip);

}
