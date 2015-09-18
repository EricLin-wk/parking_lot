package com.parking.entity;

import org.apache.commons.lang.builder.ToStringBuilder;

import java.math.BigDecimal;
import java.util.Date;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Ticket class representing tickets issued when entering the parking lot
 */
public class Ticket {

    private static AtomicLong ID_GENERATOR = new AtomicLong();

    private long ticketId;

    private int entryGateId;

    private int exitGateId;

    private Date entryDate;

    private BigDecimal parkingFee;

    private Date exitDate;

    /**
     * Creates new Ticket at given gate
     *
     * @param entryGateId Id of the issuing gate.
     */
    public Ticket(int entryGateId) {
        ticketId = ID_GENERATOR.incrementAndGet();
        this.entryGateId = entryGateId;
        entryDate = new Date();
    }

    /**
     * @return Ticket Id.
     */
    public long getTicketId() {
        return ticketId;
    }

    /**
     * @return Entry gate id of this ticket.
     */
    public int getEntryGateId() {
        return entryGateId;
    }

    /**
     * Update entry gate Id.
     *
     * @param entryGateId Gate id to update.
     */
    public void setEntryGateId(int entryGateId) {
        this.entryGateId = entryGateId;
    }

    /**
     * @return Entry date.
     */
    public Date getEntryDate() {
        return entryDate;
    }

    /**
     * @return Paking fee to be paid for this ticket.
     */
    public BigDecimal getParkingFee() {
        return parkingFee;
    }

    /**
     * @param parkingFee Update this ticket's parking fee.
     */
    public void setParkingFee(BigDecimal parkingFee) {
        this.parkingFee = parkingFee;
    }

    /**
     * @return Exit date of this ticket.
     */
    public Date getExitDate() {
        return exitDate;
    }

    /**
     * @param exitDate Exit date to update, cannot be null or before this ticket's entry date.
     */
    public void setExitDate(Date exitDate) {
        if (exitDate == null || exitDate.compareTo(entryDate) < 0)
            throw new IllegalArgumentException("Exit date cannot be null or before entry date.");
        this.exitDate = exitDate;
    }

    /**
     * @return Exit gate's id.
     */
    public int getExitGateId() {
        return exitGateId;
    }

    /**
     * @param exitGateId Exit gate id to update.
     */
    public void setExitGateId(int exitGateId) {
        this.exitGateId = exitGateId;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
