package be.vinci.ipl.chattycar.gateway;

import be.vinci.ipl.chattycar.gateway.data.AuthenticationProxy;
import be.vinci.ipl.chattycar.gateway.data.NotificationProxy;
import be.vinci.ipl.chattycar.gateway.data.TripProxy;
import be.vinci.ipl.chattycar.gateway.data.UsersProxy;
import be.vinci.ipl.chattycar.gateway.models.Credentials;
import be.vinci.ipl.chattycar.gateway.models.User;
import be.vinci.ipl.chattycar.gateway.models.UserWithCredentials;
import org.springframework.stereotype.Service;


@Service
public class GatewayService {

    private final AuthenticationProxy authenticationProxy;
    private final UsersProxy usersProxy;
    private final TripProxy tripProxy;
    private final NotificationProxy notificationProxy;

    public GatewayService(AuthenticationProxy authenticationProxy, UsersProxy usersProxy,
                          TripProxy tripProxy, NotificationProxy notificationProxy) {
        this.authenticationProxy = authenticationProxy;
        this.usersProxy = usersProxy;
        this.tripProxy = tripProxy;
        this.notificationProxy = notificationProxy;
    }

    public String connect(Credentials credentials) {
        return authenticationProxy.connect(credentials);
    }

    public String verify(String token) {
        return authenticationProxy.verify(token);
    }

    public User createOneUser(UserWithCredentials user){
        authenticationProxy.createCredentials(user.getEmail(), user.toCredentials());
        return usersProxy.createOne(user.toNoIdUser());
    }

    public User getOneUser(String email) {
        return usersProxy.readOneByEmail(email);
    }

    public User getOneUser(int id) {
        return usersProxy.readOneById(id);
    }

    public void updateOneUser(Credentials credentials) {
        authenticationProxy.updateCredentials(credentials.getEmail(), credentials);
    }

    public void updateOneUser(int userId, User user) {
        usersProxy.updateOne(userId, user);
    }

    public Credentials getOneUserCredentials(String email) {
        return authenticationProxy.getOne(email);
    }

}
