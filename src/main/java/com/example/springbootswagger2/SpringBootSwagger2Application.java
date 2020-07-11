package com.example.springbootswagger2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.example.springbootswagger2.controller.*;


@SpringBootApplication
public class SpringBootSwagger2Application {

	public static void main(String[] args) {
		
		SpringApplication.run(SpringBootSwagger2Application.class, args);
	}
}
