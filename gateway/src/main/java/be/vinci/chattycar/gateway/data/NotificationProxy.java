package be.vinci.chattycar.gateway.data;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

import be.vinci.chattycar.gateway.models.Notification;

@Repository
@FeignClient(name = "notifications")
public interface NotificationProxy {
}
