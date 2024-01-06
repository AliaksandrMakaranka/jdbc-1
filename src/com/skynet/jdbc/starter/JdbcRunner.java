package com.skynet.jdbc.starter;


import com.skynet.jdbc.starter.util.ConnectionManager;
import org.postgresql.Driver;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class JdbcRunner {
    public static void main(String[] args) throws SQLException {
        Class<Driver> driverClass = Driver.class;
        String sql = """
                SELECT *
                FROM  ticket;
                """;

        try (Connection connection = ConnectionManager.open();
             Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE)) {

            System.out.println(connection.getSchema());
            System.out.println(connection.getTransactionIsolation());

            ResultSet executeResult = statement.executeQuery(sql);

            System.out.println(executeResult);
            System.out.println(statement.getUpdateCount());

            while (executeResult.next()) {
                System.out.println(executeResult.getLong("id"));
                System.out.println(executeResult.getString("passenger_no"));
                System.out.println(executeResult.getBigDecimal("cost"));
                System.out.println("---------------");
            }
        }
    public static void main(String[] args) {
        
    }
}
