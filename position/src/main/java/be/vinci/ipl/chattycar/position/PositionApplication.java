package be.vinci.ipl.chattycar.position;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class PositionApplication {

  public static void main(String[] args) {
    SpringApplication.run(PositionApplication.class, args);
  }

}
