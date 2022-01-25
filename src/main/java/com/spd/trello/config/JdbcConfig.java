package com.spd.trello.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Configuration
@ComponentScan(basePackages = {"com.spd.trello.repository", "com.spd.trello.service"})
public class JdbcConfig  {

//    private static  hikari;

    @Bean
    public HikariDataSource createDataSource() {
        Properties properties = loadProperties();
        HikariConfig hikari = new HikariConfig();
        hikari.setJdbcUrl(properties.getProperty("jdbc.url"));
        hikari.setUsername(properties.getProperty("jdbc.username"));
        hikari.setPassword(properties.getProperty("jdbc.password"));
        hikari.setMaximumPoolSize(Integer.parseInt(properties.getProperty("jdbc.connections")));
        return new HikariDataSource(hikari);
    }

    @Bean
    private static Properties loadProperties() {
        InputStream resourceAsStream = JdbcConfig.class.getClassLoader().getResourceAsStream("application.properties");
        try {
            Properties properties = new Properties();
            properties.load(resourceAsStream);
            return properties;
        } catch (IOException e) {
            throw new IllegalArgumentException("property-file not found;" + e);
        }
    }

    @Bean
    public JdbcTemplate jdbcTemplate() {
        return new JdbcTemplate(createDataSource());
    }
}
