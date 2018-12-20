package org.codet.caseidilla;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CaseidillaApplication {

    public static void main(String[] args) {
        SpringApplication.run(CaseidillaApplication.class, args);
    }
}