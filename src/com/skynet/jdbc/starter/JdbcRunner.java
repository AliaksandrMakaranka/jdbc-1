package com.skynet.jdbc.starter;

import com.skynet.jdbc.starter.util.ConnectionManager;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class JdbcRunner {
    public static void main(String[] args) throws SQLException {
//        String flightId = "2 OR 1 = 1; DROP TABLE info;";
        String flightId = "2 or '' = ''";
        List<Long> result = getTicketByFlyId(flightId);
        System.out.println(result);
    }

    private static List<Long> getTicketByFlyId(String flightId) throws SQLException {
        String sql = """
                SELECT  id
                FROM ticket
                WHERE flight_id = %s
                """.formatted(flightId);

        List<Long> result = new ArrayList<>();

        try (Connection connection = ConnectionManager.open();
             Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {
//                result.add(resultSet.getLong("id"));
                result.add(resultSet.getObject("id", Long.class));//NULL safe
            }
        }

        return result;
    }
}
