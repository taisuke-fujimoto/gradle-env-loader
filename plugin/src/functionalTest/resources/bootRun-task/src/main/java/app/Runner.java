package app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class Runner implements CommandLineRunner {
    @Autowired
    private Environment environment;

    @Override
    public void run(String... args) throws Exception {
        Arrays.asList("VAR1", "VAR2", "VAR3")
            .forEach(name -> System.out.format("%s: %s\n", name, environment.getProperty(name)));
    }
}
