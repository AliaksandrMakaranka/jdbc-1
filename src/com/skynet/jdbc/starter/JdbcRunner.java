package com.skynet.jdbc.starter;

import com.skynet.jdbc.starter.util.ConnectionManager;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class JdbcRunner {
    public static void main(String[] args) throws SQLException {
//        String flightId = "2 OR 1 = 1; DROP TABLE info;";
//        Long flightId = 2L;
//        List<Long> result = getTicketByFlyId(flightId);
//        System.out.println(result);
//        List<Long> result = getFlightBetween(LocalDate.of(2020, 1, 1).atStartOfDay(), LocalDateTime.now());
//        System.out.println(result);
        try {
            checkMetaData();
        } finally {
            ConnectionManager.closePool();
        }
    }

    private static void checkMetaData() throws SQLException {
        try (Connection connection = ConnectionManager.get()) {
            DatabaseMetaData metaData = connection.getMetaData();
            ResultSet catalogs = metaData.getCatalogs();

            while (catalogs.next()) {
                System.out.println(catalogs.getString(1));

                ResultSet schemas = metaData.getSchemas();
                while (schemas.next()) {
                    System.out.println(schemas.getString("TABLE_SCHEM"));
                }
            }
        }
    }

    private static List<Long> getFlightBetween(LocalDateTime start, LocalDateTime end) throws SQLException {
        String sql = """
                SELECT id
                FROM flight
                WHERE departure_date BETWEEN ? AND ?
                """;
        List<Long> result = new ArrayList<>();

        try (Connection connection = ConnectionManager.get();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setFetchSize(50);
            preparedStatement.setQueryTimeout(10);
            preparedStatement.setMaxRows(100);
            System.out.println(preparedStatement);

            preparedStatement.setTimestamp(1, Timestamp.valueOf(start));
            System.out.println(preparedStatement);

            preparedStatement.setTimestamp(2, Timestamp.valueOf(end));
            System.out.println(preparedStatement);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                result.add(resultSet.getLong("id"));
            }
        }

        return result;
    }

    private static List<Long> getTicketByFlyId(Long flightId) throws SQLException {
        String sql = """
                SELECT  id
                FROM ticket
                WHERE flight_id = ?
                """;

        List<Long> result = new ArrayList<>();

        try (Connection connection = ConnectionManager.get();
             PreparedStatement prepareStatement = connection.prepareStatement(sql)) {
            prepareStatement.setLong(1, flightId);

            ResultSet resultSet = prepareStatement.executeQuery();

            while (resultSet.next()) {
//                result.add(resultSet.getLong("id"));
                result.add(resultSet.getObject("id", Long.class));//NULL safe
            }
        }

        return result;
    }

}
