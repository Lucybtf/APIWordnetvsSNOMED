package com.proyectoapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.proyectoapi.controllerwordnet.*;


@SpringBootApplication
public class SpringBootSwagger2Application {

	public static void main(String[] args) {
		
		SpringApplication.run(SpringBootSwagger2Application.class, args);
	}
}
