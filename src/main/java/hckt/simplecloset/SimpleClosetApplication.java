package hckt.simplecloset;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan("hckt.simplecloset.global.config")
public class SimpleClosetApplication {

    public static void main(String[] args) {
        SpringApplication.run(SimpleClosetApplication.class, args);
    }

}
