package com.codeinvestigator.cryptobotspring;

import com.codeinvestigator.cryptobotspring.candlecollect.CandleCollectConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({CandleCollectConfiguration.class})
public class CryptobotSpringApplication {

    public static void main(String[] args) {
        SpringApplication.run(CryptobotSpringApplication.class, args);
    }

}
