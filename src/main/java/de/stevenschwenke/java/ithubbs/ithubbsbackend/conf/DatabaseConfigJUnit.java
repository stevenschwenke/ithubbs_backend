package de.stevenschwenke.java.ithubbs.ithubbsbackend.conf;

import liquibase.integration.spring.SpringLiquibase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Configuration
@Profile({"junit", "default"})
public class DatabaseConfigJUnit {

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.h2.Driver");
        dataSource.setUrl("jdbc:h2:mem:ithubbsdb;DB_CLOSE_DELAY=-1");
        dataSource.setUsername("sa");
        dataSource.setPassword("");

        return dataSource;
    }

    @Bean
    SpringLiquibase liquibase() {

        SpringLiquibase liquibase = new SpringLiquibase();
        liquibase.setDataSource(dataSource());
        liquibase.setChangeLog("classpath:/liquibase/liquibase-changelog.xml");
        return liquibase;
    }
}
