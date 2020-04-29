package de.stevenschwenke.java.ithubbs.ithubbsbackend.conf;

import org.apache.catalina.Host;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.startup.Tomcat;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.hateoas.config.EnableHypermediaSupport;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.*;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Configuration
@EnableWebMvc
@EnableHypermediaSupport(type = EnableHypermediaSupport.HypermediaType.HAL)
public class WebConfig implements WebMvcConfigurer {

    private final Logger log = LoggerFactory.getLogger(WebConfig.class);

    private final Environment environment;

    @Autowired
    public WebConfig(Environment environment) {
        this.environment = environment;
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        String localClientURI = environment.getProperty("client.uris.local");
        String testClientURI = environment.getProperty("client.uris.test");
        String prodClientURI = environment.getProperty("client.uris.prod");
        String legacyClientURI = environment.getProperty("client.uris.legacy");

        List<String> allowedOrigins = List.of(
                Objects.requireNonNull(localClientURI),
                Objects.requireNonNull(testClientURI),
                Objects.requireNonNull(legacyClientURI),
                Objects.requireNonNull(prodClientURI));
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
        // allow header "Location" to be read by clients to enable them to read the location of an uploaded group logo
        configuration.addExposedHeader("Location");
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    /**
     * This configuration exposes the /cloudfoundryapplication endpoint which is used only by Cloudfoundry to access
     * the Spring Actuator endpoints. For details
     * see https://stevenschwenke.de/CloudfoundryNotRecognizingYourAppAsSpringBootUseEmptyContextPath
     *
     * This configuration may be removed if the application is deployed under another platform-as-a-service.
     *
     * @return Factory for Tomcat
     */
    @Bean
    public TomcatServletWebServerFactory servletWebServerFactory() {
        return new TomcatServletWebServerFactory() {

            @Override
            protected void prepareContext(Host host,
                                          ServletContextInitializer[] initializers) {
                super.prepareContext(host, initializers);
                StandardContext child = new StandardContext();
                child.addLifecycleListener(new Tomcat.FixContextListener());
                child.setPath("/cloudfoundryapplication");
                ServletContainerInitializer initializer = getServletContextInitializer(
                        getContextPath());
                child.addServletContainerInitializer(initializer, Collections.emptySet());
                child.setCrossContext(true);
                host.addChild(child);
            }

        };
    }

    private ServletContainerInitializer getServletContextInitializer(String contextPath) {
        return (c, context) -> {
            Servlet servlet = new GenericServlet() {

                @Override
                public void service(ServletRequest req, ServletResponse res)
                        throws ServletException, IOException {
                    ServletContext context = req.getServletContext()
                            .getContext(contextPath);
                    context.getRequestDispatcher("/cloudfoundryapplication").forward(req,
                            res);
                }

            };
            context.addServlet("cloudfoundry", servlet).addMapping("/*");
        };
    }
}
