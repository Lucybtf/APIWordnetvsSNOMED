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


/**
 * Class SwaggerConfig: Esta clase gestiona la configuración de la herramienta de Swagger
 * @author Lucía Batista Flores
 * @version 1.0
 */

@Configuration
@EnableSwagger2
public class SwaggerConfig {

	@Bean
	public Docket api() {

		return new Docket(DocumentationType.SWAGGER_2)

				.groupName("Wordnet").select()
				.apis(RequestHandlerSelectors.basePackage("com.proyectoapi.controllerwordnet"))
				.build().apiInfo(apiInfoWordnet());	
	}

	@Bean
	public Docket apiSnomed() {
		
		return new Docket(DocumentationType.SWAGGER_2)

				.groupName("SnomedCT").select()
				.apis(RequestHandlerSelectors.basePackage("com.proyectoapi.controllersnomed"))
				.build().apiInfo(apiInfoSnomed());
	}

	@Bean
	public Docket apiDistance() {
		return new Docket(DocumentationType.SWAGGER_2).groupName("Distance").select()
				.apis(RequestHandlerSelectors.basePackage("com.proyectoapi.controllerdistance")).build()
				.apiInfo(apiInfoDistances()).useDefaultResponseMessages(true);

	}

	private ApiInfo apiInfoWordnet() {
		return new ApiInfoBuilder().title("Wordnet Library").description("Library for use Wordnet Ontology")
				.version("Version WordnetAPI 1.0").build();
	}
	
	private ApiInfo apiInfoSnomed() {
		return new ApiInfoBuilder().title("Snomed CT Library").description("Library for use Snomed CT Ontology")
				.version("Version SnomedCTAPI 1.0").build();
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