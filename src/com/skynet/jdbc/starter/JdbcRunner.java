package com.skynet.jdbc.starter;


import com.skynet.jdbc.starter.util.ConnectionManager;
import org.postgresql.Driver;

import java.sql.Connection;
import java.sql.SQLException;

public class JdbcRunner {
    public static void main(String[] args) throws SQLException {
        Class<Driver> driverClass = Driver.class;

        try (Connection connection = ConnectionManager.open()) {
            System.out.println(connection.getTransactionIsolation());
        }
    public static void main(String[] args) {
        
    }
}
