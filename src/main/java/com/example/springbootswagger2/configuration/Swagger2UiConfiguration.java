package com.example.springbootswagger2.configuration;

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
				.apis(RequestHandlerSelectors.basePackage("com.example.springbootswagger2.wordnetcontroller"))
				//.apis(Predicates.not(RequestHandlerSelectors.basePackage("org.springframework.boot")))
				//.paths(PathSelectors.any())
				///.paths(PathSelectors.regex("/api/.*"))
				//.paths(PathSelectors.ant("/swagger2-demo"))
				.build()
				.apiInfo(apiInfo())
				.useDefaultResponseMessages(true)
				.forCodeGeneration(true);
		// @formatter:on
	}
	
	@Bean
	public Docket apiDistance() {
		// @formatter:off
		return new Docket(DocumentationType.SWAGGER_2)
				.groupName("Distance")
				.select()
				.apis(RequestHandlerSelectors.basePackage("com.example.springbootswagger2.distancecontroller"))
				//.apis(Predicates.not(RequestHandlerSelectors.basePackage("org.springframework.boot")))
				//.paths(PathSelectors.any())
				///.paths(PathSelectors.regex("/api/.*"))
				//.paths(PathSelectors.ant("/swagger2-demo"))
				.build()
				.apiInfo(apiInfo())
				.useDefaultResponseMessages(true);
		// @formatter:on
	}
	
	
	
	 private ApiInfo apiInfo() {
		  return new  ApiInfoBuilder().title("Wordnet Library").description("Library for use Wordnet").version("Version 1.0").build();
	 }

	
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
		registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
	}


}
