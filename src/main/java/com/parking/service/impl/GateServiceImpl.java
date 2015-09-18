package com.parking.service.impl;

import java.util.HashSet;
import java.util.Set;

import com.parking.entity.EntryGate;
import com.parking.entity.ExitGate;
import com.parking.entity.Gate;
import com.parking.service.GateService;

/**
 * GateService implementation. Singleton class, instance managed by Spring context.
 */
public class GateServiceImpl implements GateService {

  private Set<EntryGate> entryGateSet = new HashSet<>();
  private Set<ExitGate> exitGateSet = new HashSet<>();

  /**
   * Private constructor to prevent creating instances.
   */
  private GateServiceImpl() {
  }

  @Override
  public boolean registerGate(Gate gate) {
    boolean result = false;
    if (gate instanceof EntryGate) {
      result = entryGateSet.add((EntryGate) gate);
      if (result) {
        ((EntryGate) gate).setStatus(EntryGate.Status.READY);
      }
    }
    if (gate instanceof ExitGate) {
      result = exitGateSet.add((ExitGate) gate);
    }

    return result;
  }

  @Override
  public boolean unregisterGate(Gate gate) {
    if (gate instanceof EntryGate) {
      return entryGateSet.remove(gate);
    }
    if (gate instanceof ExitGate) {
      return exitGateSet.remove(gate);
    }
    return false;
  }

  @Override
  public void updateAllEntryGateStatus(EntryGate.Status status) {
    for (EntryGate gate : entryGateSet) {
      gate.setStatus(status);
    }
  }
}
