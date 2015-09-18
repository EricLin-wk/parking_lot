package com.parking.service.impl;

import com.parking.entity.Ticket;
import com.parking.service.TicketService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;
import java.util.Calendar;

/**
 * JUnit test for TicketServiceImpl
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:application-context.xml")
public class TicketServiceImplTest {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private TicketService ticketService;

    @Test
    public void calculateParkingFee() {
        logger.info("testParkingFee");
        int hour = 2;
        double rate = 3.50;

        Ticket ticket = new Ticket(0);
        Calendar cal = Calendar.getInstance();
        cal.setTime(ticket.getEntryDate());
        cal.add(Calendar.HOUR, hour);
        ticket.setExitDate(cal.getTime());

        BigDecimal hourlyRate = BigDecimal.valueOf(rate);
        BigDecimal result = ticketService.calculateParkingFee(ticket, hourlyRate);
        Assert.assertEquals(BigDecimal.valueOf(hour * rate).setScale(2, BigDecimal.ROUND_HALF_UP), result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void setWrongExitDate() {
        Ticket ticket = new Ticket(0);
        Calendar cal = Calendar.getInstance();
        cal.setTime(ticket.getEntryDate());
        cal.add(Calendar.HOUR, -1);
        ticket.setExitDate(cal.getTime());
    }
}
