package com.parking.service.impl;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.parking.entity.EntryGate;
import com.parking.entity.ExitGate;
import com.parking.entity.Ticket;
import com.parking.service.CapacityControlService;
import com.parking.service.GateService;

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

    //create threads to enter parking lot simultaneously
    int threads = 20;
    logger.info("Creating {} threads to enter the parking lot", threads);
    ExecutorService executor = Executors.newFixedThreadPool(threads);
    for (int i = 0; i < threads; i++) {
      executor.submit(() -> {
        Random rand = new Random();
        //find a random gate to enter
        EntryGate gate = entryList.get(rand.nextInt(numEntry));
        logger.debug("Trying to enter Gate {}", gate.getName());
        Ticket ticket = capacityControlService.enterGate(gate);
        if (ticket != null) {
          //sleep for while before exit
          int sleep = rand.nextInt(10) + 1;
          logger.info("Entered parking lot, wait for {} seconds before exit.", sleep);
          try {
            Thread.sleep(sleep * 1000);
          }
          catch (InterruptedException e) {
            logger.error(e.getMessage(), e);
          }
          //find a random gate to exit
          ExitGate exitGate = exitList.get(rand.nextInt(numExit));
          capacityControlService.exitGate(exitGate, ticket);
          logger.debug("Exit gate. Ticket:" + ticket);
        }
        else {
          logger.debug("Could not enter, lot full");
        }
      });
    }

    try {
      executor.shutdown();
      executor.awaitTermination(10, TimeUnit.MINUTES);
    }
    catch (InterruptedException e) {
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

    //create threads to enter parking lot simultaneously
    int threads = 20;
    logger.info("Creating {} threads to enter the parking lot", threads);
    ExecutorService executor = Executors.newFixedThreadPool(threads);
    for (int i = 0; i < threads; i++) {
      executor.submit(() -> {
        Random rand = new Random();
        //find a random gate to enter
        EntryGate gate = entryList.get(rand.nextInt(numEntry));
        Ticket ticket = null;
        while (ticket == null) {
          logger.debug("Trying to enter Gate {}", gate.getName());
          ticket = capacityControlService.enterGate(gate);
          if (ticket != null) {
            //sleep for while before exit
            int sleep = rand.nextInt(10) + 1;
            logger.info("Entered parking lot, wait for {} seconds before exit.", sleep);
            try {
              Thread.sleep(sleep * 1000);
            }
            catch (InterruptedException e) {
              logger.error(e.getMessage(), e);
            }
            //find a random gate to exit
            ExitGate exitGate = exitList.get(rand.nextInt(numExit));
            capacityControlService.exitGate(exitGate, ticket);
            logger.info("Exit gate. Ticket:" + ticket);
          }
          else {
            logger.info("Lot full, will wait for notification.");
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
            }
            catch (InterruptedException e) {
              logger.debug("Wait is over, try to enter again");
            }
          }
        }
      });
    }

    try {
      executor.shutdown();
      executor.awaitTermination(10, TimeUnit.MINUTES);
    }
    catch (InterruptedException e) {
      logger.error(e.getMessage(), e);
    }

  }
}
