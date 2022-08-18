package se.martenb.mymoviesback;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import se.martenb.mymoviesback.controller.MovieServiceController;

@EnabledIfEnvironmentVariable(named = "SPRING_TESTING_PROFILES_ACTIVE", matches = ".*applicationtest.*")
@ExtendWith(SpringExtension.class)
@SpringBootTest
class MovieApplicationTests {

    @Autowired
    MovieServiceController movieServiceController;

    @Test
    void contextLoads() {
        Assertions.assertThat(movieServiceController).isInstanceOf(MovieServiceController.class);
    }

}
