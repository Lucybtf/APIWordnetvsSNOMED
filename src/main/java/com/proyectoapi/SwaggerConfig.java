package com.proyectoapi;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

	@Bean
	public Docket api() {

		return new Docket(DocumentationType.SWAGGER_2)

				.groupName("Wordnet").select()
				.apis(RequestHandlerSelectors.basePackage("com.proyectoapi.controllerwordnet"))
				// .apis(Predicates.not(RequestHandlerSelectors.basePackage("com.proyectoapi.controllerdistance")))
				// .apis(Predicates.not(RequestHandlerSelectors.basePackage("com.proyectoapi.controllersnomed")))
				// .apis(Predicates.not(RequestHandlerSelectors.basePackage("org.springframework.boot")))
				// .paths(selector)
				// .paths(PathSelectors.any())
				// .paths(Predicates.not(PathSelectors.regex("/basic-error-controller.*")))
				// .paths(not(PathSelectors.regex("/error/.*")))
				/// .paths(PathSelectors.regex("/api/.*"))
				// .paths(PathSelectors.ant("/swagger2-demo"))
				.build().apiInfo(apiInfoWordnet());
		// .useDefaultResponseMessages(true)
		// .forCodeGeneration(true);
		// @formatter:on
	}

	@Bean
	public Docket apiSnomed() {
		// @formatter:off
		return new Docket(DocumentationType.SWAGGER_2)

				.groupName("SnomedCT").select()
				.apis(RequestHandlerSelectors.basePackage("com.proyectoapi.controllersnomed"))
				// .apis(Predicates.not(RequestHandlerSelectors.basePackage("com.proyectoapi.controllerdistance")))
				// .apis(Predicates.not(RequestHandlerSelectors.basePackage("com.proyectoapi.controllerwordnet")))
				.build();
		// .apiInfo(apiInfoWordnet());
	}

	@Bean
	public Docket apiDistance() {
		return new Docket(DocumentationType.SWAGGER_2).groupName("Distance").select()
				.apis(RequestHandlerSelectors.basePackage("com.proyectoapi.controllerdistance")).build()
				.apiInfo(apiInfoDistances()).useDefaultResponseMessages(true);

	}

	private ApiInfo apiInfoWordnet() {
		return new ApiInfoBuilder().title("Wordnet Library").description("Library for use Wordnet")
				.version("Version 1.0").build();
	}

	private ApiInfo apiInfoDistances() {
		return new ApiInfoBuilder().title("Distance Wordnet Library")
				.description("Library for calculate distances and measures").version("Version 1.0").build();
	}

	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
		registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
	}

}