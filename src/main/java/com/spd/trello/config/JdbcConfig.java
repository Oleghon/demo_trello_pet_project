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

    private static DataSource dataSource;

    static {
        createDataSource();
        Flyway flyway = createFlyway(dataSource);
        flyway.migrate();
    }

    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public static void createDataSource() {
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
        hikari.setMaximumPoolSize(Integer.parseInt(properties.getProperty("jdbc.connections")));
        dataSource = new HikariDataSource(hikari);
    }

    public static Flyway createFlyway(DataSource dataSource) {
        return Flyway.configure()
                .dataSource(dataSource)
                .load();
    }

    public static Properties loadProperties() throws IOException {
        InputStream resourceAsStream = JdbcConfig.class.getClassLoader().getResourceAsStream("application.properties");
        Properties properties = new Properties();
        properties.load(resourceAsStream);
        return properties;
    }
}
