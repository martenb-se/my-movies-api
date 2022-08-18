package se.martenb.mymoviesback;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MyMoviesApplication implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(MyMoviesApplication.class);

    @Value("${spring.application.name}")
    private String applicationName;

    public static void main(String[] args) {
        SpringApplication.run(MyMoviesApplication.class, args);
    }

    @Override
    public void run(String... args) {
        logger.info("Application Name: " + applicationName);
    }

}
