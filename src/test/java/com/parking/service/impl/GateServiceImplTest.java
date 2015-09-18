package com.parking.service.impl;

import com.parking.entity.EntryGate;
import com.parking.entity.ExitGate;
import com.parking.entity.Gate;
import com.parking.service.GateService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * JUnit test for GateServiceImpl
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:application-context.xml")
public class GateServiceImplTest {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private GateService gateService;

    @Test
    public void registerGate() {
        Gate gate1 = new EntryGate("Entry1");
        Assert.assertEquals(true, gateService.registerGate(gate1));

        Gate gate2 = new ExitGate("Exit2");
        Assert.assertEquals(true, gateService.registerGate(gate2));
    }

    @Test
    public void unregisterGate() {
        Gate gate1 = new EntryGate("Entry1");
        gateService.registerGate(gate1);
        Gate gate2 = new ExitGate("Exit2");

        Assert.assertEquals(true, gateService.unregisterGate(gate1));
        Assert.assertEquals(false, gateService.unregisterGate(gate2));
    }

    @Test
    public void updateAllEntryGateStatus() {
        EntryGate gate1 = new EntryGate("Entry1");
        gateService.registerGate(gate1);
        EntryGate gate2 = new EntryGate("Entry2");
        gateService.registerGate(gate2);
        EntryGate gate3 = new EntryGate("Entry3");

        gateService.updateAllEntryGateStatus(EntryGate.Status.READY);
        Assert.assertEquals(EntryGate.Status.READY, gate1.getStatus());
        Assert.assertEquals(EntryGate.Status.READY, gate2.getStatus());
        Assert.assertEquals(EntryGate.Status.CLOSED, gate3.getStatus());
    }
}
