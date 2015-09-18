package com.parking.service;

import com.parking.entity.EntryGate;
import com.parking.entity.ExitGate;
import com.parking.entity.Ticket;

import java.math.BigDecimal;
import java.util.Observer;

/**
 * Parking lot capacity related service.
 */
public interface CapacityControlService {

    /**
     * @return Hourly parking rate.
     */
    BigDecimal getHourlyRate();

    /**
     * Tries to allow a car to enter the given gate.
     *
     * @param gate Target gate to enter.
     * @return Parking ticket if parking lot is not full. Otherwise return null.
     */
    Ticket enterGate(EntryGate gate);

    /**
     * Wait for the parking lot to become available to enter.
     *
     * @param observer Observer to be notified when parking lot is not full.
     */
    void waitToEnter(Observer observer);

    /**
     * Exit at the given gate.
     *
     * @param gate   Target gate to exit.
     * @param ticket Ticket when entering the parking lot.
     * @return Ticket with exitDate and parking fee set, to be used as receipt.
     */
    Ticket exitGate(ExitGate gate, Ticket ticket);

    /**
     * @return Total capacity of this parking lot.
     */
    int getTotalCapacity();

    /**
     * @return Current capacity of the parking lot.
     */
    int getCurrentCapacity();
}
