package com.parking.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Observer;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.parking.entity.EntryGate;
import com.parking.entity.ExitGate;
import com.parking.entity.Ticket;
import com.parking.service.CapacityControlService;
import com.parking.service.GateService;
import com.parking.service.TicketService;
import com.parking.util.ObservableParkingLot;

/**
 * CapacityControlService implementation. Singleton class, instance managed by Spring context.
 */
@Service
public class CapacityControlServiceImpl implements CapacityControlService {

  private Logger logger = LoggerFactory.getLogger(this.getClass());

  @Autowired
  private TicketService ticketService;

  @Autowired
  private GateService gateService;

  /**
   * Total totalCapacity of the parking lot.
   */
  private int totalCapacity;

  /**
   * Hourly rate to park.
   */
  private BigDecimal hourlyRate;

  /**
   * Current number of occupants.
   */
  private AtomicInteger occupantCounter = new AtomicInteger();

  /**
   * Observable helper for registering cars waiting to enter lot.
   */
  private ObservableParkingLot observable = new ObservableParkingLot();

  /**
   * Private constructor to prevent creating instances.
   */
  private CapacityControlServiceImpl() {
  }

  /**
   * Private constructor to prevent creating instances.
   *
   * @param totalCapacity Total capacity of parking lot.
   * @param hourlyRate    Hourly parking rate.
   */
  private CapacityControlServiceImpl(int totalCapacity, double hourlyRate) {
    this.totalCapacity = totalCapacity;
    this.hourlyRate = BigDecimal.valueOf(hourlyRate).setScale(2, BigDecimal.ROUND_HALF_UP);
  }

  @Override
  public Ticket enterGate(EntryGate gate) {
    //check gate status
    if (gate.getStatus() != EntryGate.Status.READY)
      return null;
    //try to increase counter and see if within capacity
    int newCount = occupantCounter.incrementAndGet();
    logger.debug("increase capacity to {}", newCount);
    if (newCount > totalCapacity) {
      newCount = occupantCounter.decrementAndGet(); //exceed max capacity, rollback
      logger.debug("exceed capacity, roll back capacity to {}", newCount);
      gateService.updateAllEntryGateStatus(EntryGate.Status.CLOSED);
      return null;
    }
    return new Ticket(gate.getGateId());
  }

  @Override
  public void waitToEnter(Observer observer) {
    observable.addObserver(observer);
  }

  @Override
  public Ticket exitGate(ExitGate gate, Ticket ticket) {
    int newCount = occupantCounter.decrementAndGet();
    //notify observers space opened up
    if (newCount < totalCapacity) {
      observable.spaceAvailable();
      gateService.updateAllEntryGateStatus(EntryGate.Status.READY);
    }
    //update ticket
    ticket.setExitGateId(gate.getGateId());
    ticket.setExitDate(new Date());
    ticket.setParkingFee(ticketService.calculateParkingFee(ticket, hourlyRate));

    return ticket;
  }

  @Override
  public int getTotalCapacity() {
    return totalCapacity;
  }

  @Override
  public int getCurrentCapacity() {
    return occupantCounter.get();
  }

  @Override
  public BigDecimal getHourlyRate() {
    return hourlyRate;
  }
}
