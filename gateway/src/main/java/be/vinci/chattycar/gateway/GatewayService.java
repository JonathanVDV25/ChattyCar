package be.vinci.chattycar.gateway;

import org.springframework.stereotype.Service;

import be.vinci.chattycar.gateway.data.*;
import be.vinci.chattycar.gateway.models.*;

@Service
public class GatewayService {

    private final AuthenticationProxy authenticationProxy;
    private final UsersProxy usersProxy;
    private final TripsProxy tripProxy;
    private final NotificationProxy notificationProxy;

    public GatewayService(AuthenticationProxy authenticationProxy, UsersProxy usersProxy,
                          TripsProxy tripProxy, NotificationProxy notificationProxy) {
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

}
