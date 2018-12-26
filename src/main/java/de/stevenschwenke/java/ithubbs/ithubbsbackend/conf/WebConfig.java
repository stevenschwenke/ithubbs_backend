package de.stevenschwenke.java.ithubbs.ithubbsbackend.conf;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;
import java.util.Objects;

@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {

    private final Logger log = LoggerFactory.getLogger(WebConfig.class);

    private final Environment environment;

    @Autowired
    public WebConfig(Environment environment) {
        this.environment = environment;
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        String localURI = environment.getProperty("client.uris.local");
        String clientURI = environment.getProperty("client.uris.test");

        List<String> allowedOrigins = List.of(Objects.requireNonNull(localURI), Objects.requireNonNull(clientURI));
        log.info("CORS allowed origins: " + StringUtils.join(allowedOrigins, ", "));

        final CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(allowedOrigins);
        configuration.setAllowedMethods(List.of("HEAD", "GET", "POST", "PUT", "DELETE", "PATCH"));
        // setAllowCredentials(true) is important, otherwise:
        // The value of the 'Access-Control-Allow-Origin' header in the response must not be the wildcard '*' when the request's credentials mode is 'include'.
        configuration.setAllowCredentials(true);
        // setAllowedHeaders is important! Without it, OPTIONS preflight request
        // will fail with 403 Invalid CORS request
        configuration.setAllowedHeaders(List.of("Authorization", "Cache-Control", "Content-Type"));
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}