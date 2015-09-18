package com.parking.service.impl;

import com.parking.entity.EntryGate;
import com.parking.entity.ExitGate;
import com.parking.entity.Ticket;
import com.parking.service.CapacityControlService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


/**
 * JUnit test for CapacityControlServiceImpl
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:application-context.xml")
public class CapacityControlServiceImplTest {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private CapacityControlService capacityControlService;

    @Before
    public void setUp() {
    }

    @Test
    public void enterGate() {
        EntryGate entryGate1 = new EntryGate("EntryGate 1");
        entryGate1.setStatus(EntryGate.Status.READY);
        for (int i = 0; i < capacityControlService.getTotalCapacity(); i++) {
            Ticket ticket = capacityControlService.enterGate(entryGate1);
            logger.debug("enterGate ticket: " + ticket);
            Assert.assertNotNull(ticket);
        }
        Assert.assertNull(capacityControlService.enterGate(entryGate1));
    }

    @Test
    public void exitGate() {
        ExitGate exitGate = new ExitGate("ExitGate 1");
        Ticket ticket = new Ticket(0);

        capacityControlService.exitGate(exitGate, ticket);
        Assert.assertNotNull(ticket.getExitGateId());
        Assert.assertNotNull(ticket.getExitDate());
        Assert.assertNotNull(ticket.getParkingFee());
    }

}
