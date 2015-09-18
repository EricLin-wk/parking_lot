package com.parking.service;

import com.parking.entity.EntryGate;
import com.parking.entity.Gate;

/**
 * Gate related service.
 */
public interface GateService {


    /**
     * Registers the given gate to the system.
     *
     * @param gate Gate to register.
     * @return Registration success or fail.
     */
    boolean registerGate(Gate gate);

    /**
     * Unregister the given gate to the system.
     *
     * @param gate Gate to unregister.
     * @return Un-registration success or fail.
     */
    boolean unregisterGate(Gate gate);


    /**
     * Update status of all registered entry gate to the given status.
     *
     * @param status Status to update to.
     */
    void updateAllEntryGateStatus(EntryGate.Status status);

}
