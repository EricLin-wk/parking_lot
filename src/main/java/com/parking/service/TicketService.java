package com.parking.service;

import com.parking.entity.Ticket;

import java.math.BigDecimal;

/**
 * Ticket related service.
 */
public interface TicketService {

    /**
     * Calculate parking fee for the given ticket, charged on a per hour basis.
     *
     * @param ticket     Ticket containing entry and exit time.
     * @param hourlyRate Hourly rate for parking.
     * @return Total parking fee for the ticket.
     */
    BigDecimal calculateParkingFee(Ticket ticket, BigDecimal hourlyRate);


}
