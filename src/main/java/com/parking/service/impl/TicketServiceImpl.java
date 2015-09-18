package com.parking.service.impl;

import com.parking.entity.Ticket;
import com.parking.service.TicketService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * Implementation for TicketService. Singleton class, instance managed by Spring context.
 */
@Service
public class TicketServiceImpl implements TicketService {

    /**
     * Private constructor to prevent creating instances.
     */
    private TicketServiceImpl() {
    }

    @Override
    public BigDecimal calculateParkingFee(Ticket ticket, BigDecimal hourlyRate) {
        if (ticket == null || ticket.getEntryDate() == null || ticket.getExitDate() == null)
            throw new IllegalArgumentException("Ticket must contain entry date and exit date.");
        if (ticket.getExitDate().compareTo(ticket.getEntryDate()) < 0)
            throw new IllegalArgumentException("Ticket's exit date must be later than entry date");
        if (hourlyRate == null || hourlyRate.compareTo(BigDecimal.ZERO) < 0)
            throw new IllegalArgumentException("Hourly rate cannot be null or negative.");


        double parkedHour = Math.ceil(ticket.getExitDate().getTime() - ticket.getEntryDate().getTime()) / (1000 * 60 * 60);

        return BigDecimal.valueOf(parkedHour).multiply(hourlyRate).setScale(2, BigDecimal.ROUND_HALF_UP);
    }
}
