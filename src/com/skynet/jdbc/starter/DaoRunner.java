package com.skynet.jdbc.starter;

import com.skynet.jdbc.starter.dao.TicketDao;
import com.skynet.jdbc.starter.entity.Ticket;

import java.math.BigDecimal;

public class DaoRunner {
    public static void main(String[] args) {
//        saveTest();
        TicketDao ticketDao = TicketDao.getInstance();
        boolean deleteResult = ticketDao.delete(56L);
        System.out.println(deleteResult);
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
