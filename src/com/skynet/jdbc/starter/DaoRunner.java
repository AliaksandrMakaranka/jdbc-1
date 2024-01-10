package com.skynet.jdbc.starter;

import com.skynet.jdbc.starter.dao.TicketDao;
import com.skynet.jdbc.starter.entity.Ticket;

import java.math.BigDecimal;
import java.util.Optional;

public class DaoRunner {
    public static void main(String[] args) {
        var tickets = TicketDao.getInstance().findAll();

        System.out.println(tickets);
    }

    private static void updateTest() {
        TicketDao ticketDao = TicketDao.getInstance();
        Optional<Ticket> maybeTicket = ticketDao.findById(2L);
        System.out.println(maybeTicket);

        maybeTicket.ifPresent(ticket -> {
            ticket.setCost(BigDecimal.valueOf(188.88));
            ticketDao.update(ticket);
        });
    }

    private static void saveTest() {
        TicketDao ticketDao = TicketDao.getInstance();
        Ticket ticket = new Ticket();
        ticket.setPassengerNo("1234567");
        ticket.setPassengerName("test");
        ticket.setFlightId(3L);
        ticket.setSeatNo("B3");
        ticket.setCost(BigDecimal.TEN);
        Ticket saveTicket = ticketDao.save(ticket);
        System.out.println(saveTicket);
    }
}
