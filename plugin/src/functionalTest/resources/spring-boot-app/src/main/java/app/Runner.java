package app;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class Runner implements CommandLineRunner {

    @Value("${DESCRIPTION}")
    private String description;

    @Override
    public void run(String... args) throws Exception {
        System.out.println(description);
    }
}
