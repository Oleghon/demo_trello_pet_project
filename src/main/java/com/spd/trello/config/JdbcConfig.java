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

    private static HikariConfig hikari;
    private static DataSource dataSource;

    static {
        createDataSource();
        Flyway flyway = createFlyway(dataSource);
        flyway.migrate();
    }

    private static void createDataSource() {
        Properties properties = loadProperties();
        hikari = new HikariConfig();
        hikari.setJdbcUrl(properties.getProperty("jdbc.url"));
        hikari.setUsername(properties.getProperty("jdbc.username"));
        hikari.setPassword(properties.getProperty("jdbc.password"));
        hikari.setMaximumPoolSize(Integer.parseInt(properties.getProperty("jdbc.connections")));
        dataSource = new HikariDataSource(hikari);
    }

    private static Flyway createFlyway(DataSource dataSource) {
        return Flyway.configure()
                .dataSource(dataSource)
                .load();
    }

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

    public static <T> T execute(ConnectionCallback<T> callback) {
        try (Connection connection = dataSource.getConnection()) {
            return callback.doInConnection(connection);
        } catch (SQLException e) {
            throw new IllegalStateException("Error during execution.", e);
        }
    }

    public interface ConnectionCallback<T> {
        T doInConnection(Connection conn) throws SQLException;
    }
}
