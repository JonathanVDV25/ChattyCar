package be.vinci.ipl.chattycar.gateway;

import be.vinci.ipl.chattycar.gateway.data.AuthenticationProxy;
import be.vinci.ipl.chattycar.gateway.data.NotificationProxy;
import be.vinci.ipl.chattycar.gateway.data.TripProxy;
import be.vinci.ipl.chattycar.gateway.data.UsersProxy;
import be.vinci.ipl.chattycar.gateway.models.Credentials;
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

    public void createOneUser(UserWithCredentials user){
        usersProxy.createOne(user.toNoIdUser());
        authenticationProxy.createCredentials(user.getEmail(), user.toCredentials());
    }

}
