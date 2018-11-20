package com.mls.task.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class DBConnectionProvider {
    private static final Logger LOGGER = LoggerFactory.getLogger(DBConnectionProvider.class);

    private static DBConnectionProvider provider;

    private static final Lock initializationLock = new ReentrantLock();

    private String dbDriver;

    private String dbUrl;

    private String dbUsername;

    private String dbPassword;

    private Connection connection;


    private DBConnectionProvider() {
        loadConfigProperties();
        try {
            Class.forName(dbDriver);
        } catch (ClassNotFoundException e) {
            LOGGER.error(String.format("Exception occurred on database driver %s loading time", dbDriver), e);
        }

    }

    private static DBConnectionProvider getInstance() {
        if (provider == null) {
            initializationLock.lock();
            try {
                if (provider == null) {
                    provider = new DBConnectionProvider();
                }
            } finally {
                initializationLock.unlock();
            }

            return provider;

        }
        return provider;
    }

    private Connection createConnection() {
        final Lock connectionLock;
        try {
            if (connection == null || connection.isClosed()) {
                connectionLock = new ReentrantLock();
                try {
                    connectionLock.lock();
                    connection = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
                } finally {
                    connectionLock.unlock();
                }
            }
        } catch (SQLException e) {
            LOGGER.error(String.format("Exception occurred on connection creating time with url:%s username:%s, password:%s", dbUrl, dbUsername, dbPassword), e);
        }
        return connection;

    }

    public static Connection getConnection() {
        return getInstance().createConnection();
    }



    private void loadConfigProperties() {

        InputStream inStream = DBConnectionProvider.class.
                getClassLoader()
                .getResourceAsStream
                        ("dbConfig.properties");
        Properties dbProps = new Properties();
        try {
            dbProps.load(inStream);
        } catch (IOException e) {
            LOGGER.error("Exception occurred on DB config properties loading time", e);
        }
        dbDriver = dbProps.getProperty("db.driver");
        dbUrl = dbProps.getProperty("db.url");
        dbUsername = dbProps.getProperty("db.username");
        dbPassword = dbProps.getProperty("db.password");
    }
}
