package org.example.stamppaw_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class StamppawBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(StamppawBackendApplication.class, args);
    }

}
