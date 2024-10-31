package hello.aiofirstuser;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class AioFirstUserApplication {

    public static void main(String[] args) {
        SpringApplication.run(AioFirstUserApplication.class, args);
    }

}
