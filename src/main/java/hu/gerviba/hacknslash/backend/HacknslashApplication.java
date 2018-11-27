package hu.gerviba.hacknslash.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main class of the server
 * @author Gergely Szabó
 */

@SpringBootApplication
public class HacknslashApplication {

    /**
     * Entrypoint of the application
     * @param args Command line arguments
     */
	public static void main(String[] args) {
		SpringApplication.run(HacknslashApplication.class, args);
	}
}
