package com.eburtis.assurance;

import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

import static io.swagger.v3.oas.annotations.enums.SecuritySchemeIn.HEADER;
import static io.swagger.v3.oas.annotations.enums.SecuritySchemeType.APIKEY;

@SpringBootApplication
@SecurityScheme(name = "Authorization", scheme = "basic", type = APIKEY, in = HEADER)
public class ABASSURANCEApplication extends SpringBootServletInitializer {
	public static final Logger log = LoggerFactory.getLogger(ABASSURANCEApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(ABASSURANCEApplication.class, args);

		log.info("""

				************************************************************************************************
				WELCOME TO AB ASSURANCE
				Environment : Production / Dev / Test
				Started at  : {}
				************************************************************************************************
				""", java.time.LocalDateTime.now());
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(ABASSURANCEApplication.class);
	}
}
