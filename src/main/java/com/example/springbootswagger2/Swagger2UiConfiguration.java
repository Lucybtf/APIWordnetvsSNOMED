package com.example.springbootswagger2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.google.common.base.Predicates;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class Swagger2UiConfiguration extends WebMvcConfigurerAdapter  {

	
	@Bean
	public Docket apiWordnet() {
		// @formatter:off
		return new Docket(DocumentationType.SWAGGER_2)
				.groupName("Wordnet")
				.select()
				.apis(RequestHandlerSelectors.basePackage("com.example.springbootswagger2.controller"))
				.apis(Predicates.not(RequestHandlerSelectors.basePackage("com.example.springbootswagger2.controllerdistance")))
				//.paths(PathSelectors.any())
				///.paths(PathSelectors.regex("/api/.*"))
				//.paths(PathSelectors.ant("/swagger2-demo"))
				.build()
				.apiInfo(apiInfoWordnet());
				//.useDefaultResponseMessages(true)
				//.forCodeGeneration(true);
		// @formatter:on
	}
	
	
	
	@Bean
	public Docket apiDistance() {
		// @formatter:off
		return new Docket(DocumentationType.SWAGGER_2)
				.groupName("Distance")
				.select()
				.apis(RequestHandlerSelectors.basePackage("com.example.springbootswagger2.controllerdistance"))
				//.apis(Predicates.not(RequestHandlerSelectors.basePackage("org.springframework.boot")))
				//.paths(PathSelectors.any())
				///.paths(PathSelectors.regex("/api/.*"))
				//.paths(PathSelectors.ant("/swagger2-demo"))
				.build()
				.apiInfo(apiInfoWNDistance())
				.useDefaultResponseMessages(true);
		// @formatter:on
	}
	
	
	
	 private ApiInfo apiInfoWordnet() {
		  return new  ApiInfoBuilder().title("Wordnet Library").description("Library for use Wordnet").version("Version 1.0").build();
	 }
	 
	 
	 private ApiInfo apiInfoWNDistance() {
		  return new  ApiInfoBuilder().title("Distance Wordnet Library").description("Library for calculate distances and measures").version("Version 1.0").build();
	 }
	 
}