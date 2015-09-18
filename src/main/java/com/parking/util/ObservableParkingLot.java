package com.parking.util;

import com.parking.entity.EntryGate;

import java.util.Observable;

/**
 * Observer pattern helper class.
 */
public class ObservableParkingLot extends Observable {

    /**
     * Mark the Observable state to changed, and notify observers that space is available.
     */
    public void spaceAvailable() {
        super.setChanged();
        super.notifyObservers(EntryGate.Status.READY);
        super.deleteObservers();
    }

}
