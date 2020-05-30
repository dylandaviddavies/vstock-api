package me.dylandavies.vstockapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class VstockApiApplication {

	@SuppressWarnings("resource")
	public static void main(String[] args) {
		SpringApplication.run(VstockApiApplication.class, args);
	}

}
