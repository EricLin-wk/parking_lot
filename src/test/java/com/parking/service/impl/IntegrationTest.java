package com.parking.service.impl;

import com.parking.entity.EntryGate;
import com.parking.entity.ExitGate;
import com.parking.entity.Ticket;
import com.parking.service.CapacityControlService;
import com.parking.service.GateService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Integration Test
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:application-context.xml")
public class IntegrationTest {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private CapacityControlService capacityControlService;
    @Autowired
    private GateService gateService;

    @Test
    public void multiThreadTest() {
        //initialize the gates
        int numEntry = 3;
        ArrayList<EntryGate> entryList = new ArrayList<>();
        for (int i = 0; i < numEntry; i++) {
            EntryGate gate = new EntryGate("EntryGate " + (i + 1));
            gateService.registerGate(gate);
            entryList.add(gate);
        }

        int numExit = 5;
        ArrayList<ExitGate> exitList = new ArrayList<>();
        for (int i = 0; i < numExit; i++) {
            ExitGate gate = new ExitGate("ExitGate " + (i + 1));
            gateService.registerGate(gate);
            exitList.add(gate);
        }

        Random rand = new Random(1);
        //create threads to enter parking lot simultaneously
        int threads = 20;
        ExecutorService executor = Executors.newFixedThreadPool(threads);
        for (int i = 0; i < threads; i++) {
            executor.submit(() -> {
                //find a random gate to enter
                EntryGate gate = entryList.get(rand.nextInt(numEntry));
                logger.debug("Trying to enter Gate {}", gate.getName());
                Ticket ticket = capacityControlService.enterGate(gate);
                if (ticket != null) {
                    //sleep for while before exit
                    int sleep = rand.nextInt(10) + 1;
                    logger.debug("Got ticket, wait for {} seconds to exit.", sleep);
                    try {
                        Thread.sleep(sleep * 1000);
                    } catch (InterruptedException e) {
                        logger.error(e.getMessage(), e);
                    }
                    //find a random gate to exit
                    ExitGate exitGate = exitList.get(rand.nextInt(numExit));
                    capacityControlService.exitGate(exitGate, ticket);
                    logger.debug("Exit gate. Ticket:" + ticket);
                } else {
                    logger.debug("Could not enter gate, lot full");
                }
            });
        }

        try {
            executor.shutdown();
            executor.awaitTermination(10, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            logger.error(e.getMessage(), e);
        }
    }

    @Test
    public void multiThreadTestWithRetry() {
        //initialize the gates
        int numEntry = 3;
        ArrayList<EntryGate> entryList = new ArrayList<>();
        for (int i = 0; i < numEntry; i++) {
            EntryGate gate = new EntryGate("EntryGate " + (i + 1));
            gateService.registerGate(gate);
            entryList.add(gate);
        }

        int numExit = 5;
        ArrayList<ExitGate> exitList = new ArrayList<>();
        for (int i = 0; i < numExit; i++) {
            ExitGate gate = new ExitGate("ExitGate " + (i + 1));
            gateService.registerGate(gate);
            exitList.add(gate);
        }

        Random rand = new Random(1);
        //create threads to enter parking lot simultaneously
        int threads = 5;
        ExecutorService executor = Executors.newFixedThreadPool(threads);
        for (int i = 0; i < threads; i++) {
            executor.submit(() -> {
                //find a random gate to enter
                EntryGate gate = entryList.get(rand.nextInt(numEntry));
                Ticket ticket = null;
                while (ticket == null) {
                    logger.debug("Trying to enter Gate {}", gate.getName());
                    ticket = capacityControlService.enterGate(gate);
                    if (ticket != null) {
                        //sleep for while before exit
                        int sleep = 5;
                        logger.info("Got ticket, wait for {} seconds to exit.", sleep);
                        try {
                            Thread.sleep(sleep * 1000);
                        } catch (InterruptedException e) {
                            logger.error(e.getMessage(), e);
                        }
                        //find a random gate to exit
                        ExitGate exitGate = exitList.get(rand.nextInt(numExit));
                        capacityControlService.exitGate(exitGate, ticket);
                        logger.info("Exit gate. Ticket:" + ticket);
                    } else {
                        logger.info("Could not enter gate, will wait");
                        //register observer and sleep
                        Thread currentThread = Thread.currentThread();
                        capacityControlService.waitToEnter((Observable o, Object arg) -> {
                            if (arg == EntryGate.Status.READY) {
                                logger.debug("Parking space available, wake up thread.");
                                currentThread.interrupt();
                            }
                        });
                        try {
                            Thread.sleep(60 * 1000);
                        } catch (InterruptedException e) {
                            logger.debug("Wait is over, try to enter again");
                        }
                    }
                }
            });
        }

        try {
            executor.shutdown();
            executor.awaitTermination(10, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            logger.error(e.getMessage(), e);
        }

    }
}
