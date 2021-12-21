package com.spd.trello.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.flywaydb.core.Flyway;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class JdbcConfig {

    {
        Flyway flyway = createFlyway(createDataSource());
        flyway.migrate();
    }

    public Connection getConnection() throws SQLException {
        return createDataSource().getConnection();
    }

    public DataSource createDataSource() {
        Properties properties = null;
        try {
            properties = loadProperties();
        } catch (IOException e) {
            e.printStackTrace();
        }
        HikariConfig hikari = new HikariConfig();

        hikari.setJdbcUrl(properties.getProperty("jdbc.url"));
        hikari.setUsername(properties.getProperty("jdbc.username"));
        hikari.setPassword(properties.getProperty("jdbc.password"));

        return new HikariDataSource(hikari);
    }

    public Flyway createFlyway(DataSource dataSource) {
        return Flyway.configure()
                .dataSource(dataSource)
                .load();
    }

    private Properties loadProperties() throws IOException {
        InputStream resourceAsStream = JdbcConfig.class.getClassLoader().getResourceAsStream("application.properties");
        Properties properties = new Properties();
        properties.load(resourceAsStream);
        return properties;
    }
}
