package app;

import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        Arrays.asList("VAR1", "VAR2", "VAR3")
            .forEach(name -> System.out.format("%s: %s\n", name, System.getenv(name)));
    }
}
