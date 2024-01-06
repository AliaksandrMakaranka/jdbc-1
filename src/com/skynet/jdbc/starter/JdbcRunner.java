package com.skynet.jdbc.starter;


import com.skynet.jdbc.starter.util.ConnectionManager;
import org.postgresql.Driver;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class JdbcRunner {
    public static void main(String[] args) throws SQLException {
        Class<Driver> driverClass = Driver.class;
        String sql = """
                UPDATE info
                SET data = 'TestTest'
                WHERE id = 5
                RETURNING *;
                """;

        try (Connection connection = ConnectionManager.open();
             Statement statement = connection.createStatement()) {
            System.out.println(connection.getSchema());
            System.out.println(connection.getTransactionIsolation());
            boolean executeResult = statement.execute(sql);
            System.out.println(executeResult);
            System.out.println(statement.getUpdateCount());
        }
    public static void main(String[] args) {
        
    }
}
