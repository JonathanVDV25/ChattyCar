package be.vinci.ipl.chattycar.passengers.data;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Repository;

@Repository
@FeignClient(name = "users")
public interface UsersProxy {

}
