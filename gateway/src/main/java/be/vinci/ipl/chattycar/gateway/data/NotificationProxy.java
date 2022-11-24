package be.vinci.ipl.chattycar.gateway.data;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Repository;

@Repository
@FeignClient(name = "notifications")
public interface NotificationProxy {
}
