package com.itlabs.api.configuration;

import java.util.Collections;
import java.util.Date;
import org.springframework.boot.info.BuildProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spring.web.plugins.Docket;
import static com.itlabs.api.controllers.Routes.API_ROOT_ROUTE;
import static springfox.documentation.builders.PathSelectors.regex;
import static springfox.documentation.spi.DocumentationType.SWAGGER_2;

@Configuration
public class SwaggerConfig {

	private final BuildProperties buildProperties;
	static final String ACTUATOR = ".*/actuator.*";

	public SwaggerConfig(BuildProperties buildProperties) {
		this.buildProperties = buildProperties;
	}

	@Bean
	public Docket api() {
		return new Docket(SWAGGER_2).groupName("api").select().apis(RequestHandlerSelectors.any())
				.paths(regex(String.format(".*%s.*", API_ROOT_ROUTE))).build()
				.apiInfo(apiInfo(buildProperties.getName()));
	}

	@Bean
	public Docket actuatorApi() {
		return new Docket(SWAGGER_2).groupName("monitoring-api").select().apis(RequestHandlerSelectors.any())
				.paths(regex(ACTUATOR)).build().apiInfo(apiInfo("Actuator"));
	}

	private ApiInfo apiInfo(String name) {

		return new ApiInfo(name,
				String.format(" Application Build at %s and Started at %s", buildProperties.getTime(),
						new Date().toInstant()),
				buildProperties.getVersion(), "",
				new Contact("Jovica Krstevski", "http://it-labs.com", "jovica.krstevski@it-labs.com"), "MIT",
				"https://github.com/IT-Labs/backyard/blob/master/LICENSE", Collections.emptyList());
	}

}
