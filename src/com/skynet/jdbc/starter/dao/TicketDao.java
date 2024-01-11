package com.skynet.jdbc.starter.dao;

import com.skynet.jdbc.starter.dto.TicketFilter;
import com.skynet.jdbc.starter.entity.Ticket;
import com.skynet.jdbc.starter.exception.DaoException;
import com.skynet.jdbc.starter.util.ConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.joining;

public class TicketDao {
    private static final TicketDao INSTANCE = new TicketDao();
    private static final String DELETE_SQL = """
            DELETE FROM ticket
            WHERE id = ?     
            """;
    private static final String SAVE_SQL = """
                INSERT INTO ticket(passenger_no, passenger_name, flight_id, seat_no, cost) 
                VALUES (?, ?, ?, ?, ?)      
            """;
    public static final String UPDATE_SQL = """
            UPDATE ticket
            SET passenger_no = ?,
            passenger_name = ?,
            flight_id = ?,
            seat_no = ?,
            cost = ?
            WHERE id = ?
            """;

    private static final String FIND_ALL_SQL = """
            SELECT id,
            passenger_no,
            passenger_name,
            flight_id,
            seat_no,
            cost
            FROM ticket
            """;


    private static final String FIND_BY_ID_SQL = FIND_ALL_SQL + """
            WHERE id = ?     
            """;

    private TicketDao() {
    }

    public List<Ticket> findAll(TicketFilter filter) {
        List<Object> parameters = new ArrayList<>();
        List<String> whereSql = new ArrayList<>();

        if (filter.seatNo() != null) {
            whereSql.add("seat_no LIKE ?");
            parameters.add("%" + filter.seatNo() + "%");
        }

        if (filter.passengerName() != null) {
            whereSql.add("passenger_name = ?");
            parameters.add(filter.passengerName());
        }
        parameters.add(filter.limit());
        parameters.add(filter.offset());
        String where = whereSql.stream()
                .collect(joining(" AND ", " WHERE ", " LIMIT ? OFFSET ? "));

        var sql = FIND_ALL_SQL + where;

        try (Connection connection = ConnectionManager.get();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            for (int i = 0; i < parameters.size(); i++) {
                preparedStatement.setObject(i + 1, parameters.get(i));
            }
            System.out.println(preparedStatement);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Ticket> tickets = new ArrayList<>();
            while (resultSet.next()) {
                tickets.add(buildTicket(resultSet));
            }
            return tickets;
        } catch (SQLException throwables) {
            throw new DaoException(throwables);
        }
    }

    public List<Ticket> findAll() {
        try (Connection connection = ConnectionManager.get();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL_SQL)) {
            ResultSet resultSet = preparedStatement.executeQuery();

            List<Ticket> tickets = new ArrayList<>();

            while (resultSet.next()) {
                tickets.add(buildTicket(resultSet));
            }

            return tickets;
        } catch (SQLException throwables) {
            throw new DaoException(throwables);
        }
    }

    public Optional<Ticket> findById(Long id) {
        try (Connection connection = ConnectionManager.get();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_ID_SQL)) {
            preparedStatement.setLong(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();
            Ticket ticket = null;

            if (resultSet.next()) {
                ticket = buildTicket(resultSet);
            }

            return Optional.ofNullable(ticket);
        } catch (SQLException throwables) {
            throw new DaoException(throwables);
        }
    }

    private static Ticket buildTicket(ResultSet resultSet) throws SQLException {
        return new Ticket(
                resultSet.getLong("id"),
                resultSet.getString("passenger_no"),
                resultSet.getString("passenger_name"),
                resultSet.getLong("flight_id"),
                resultSet.getString("seat_no"),
                resultSet.getBigDecimal("cost")
        );
    }

    public void update(Ticket ticket) {
        try (Connection connection = ConnectionManager.get();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_SQL)) {
            preparedStatement.setString(1, ticket.getPassengerNo());
            preparedStatement.setString(2, ticket.getPassengerName());
            preparedStatement.setLong(3, ticket.getFlightId());
            preparedStatement.setString(4, ticket.getSeatNo());
            preparedStatement.setBigDecimal(5, ticket.getCost());
            preparedStatement.setLong(6, ticket.getId());

            preparedStatement.executeUpdate();
        } catch (SQLException throwables) {
            throw new DaoException(throwables);
        }
    }

    public Ticket save(Ticket ticket) {
        try (Connection connection = ConnectionManager.get();
             PreparedStatement preparedStatement = connection.prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, ticket.getPassengerNo());
            preparedStatement.setString(2, ticket.getPassengerName());
            preparedStatement.setLong(3, ticket.getFlightId());
            preparedStatement.setString(4, ticket.getSeatNo());
            preparedStatement.setBigDecimal(5, ticket.getCost());

            preparedStatement.executeUpdate();

            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                ticket.setId(generatedKeys.getLong("id"));
            }

            return ticket;
        } catch (SQLException throwables) {
            throw new DaoException(throwables);
        }
    }

    public boolean delete(Long id) {
        try (Connection connection = ConnectionManager.get();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_SQL)) {
            preparedStatement.setLong(1, id);

            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException throwables) {
            throw new DaoException(throwables);
        }
    }

    public static TicketDao getInstance() {
        return INSTANCE;
    }

}
