package com.parking.entity;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Class for entry gates into the parking lot.
 */
public class EntryGate extends Gate {

  /**
   * Initialize status to CLOSED.
   */
  private Status status = Status.CLOSED;

  /**
   * Constructor.
   *
   * @param name Gate name.
   */
  public EntryGate(final String name) {
    super(name);
  }

  /**
   * Get this gate's status.
   *
   * @return Gate status.
   */
  public Status getStatus() {
    return status;
  }

  /**
   * Update this gate's status.
   *
   * @param status Status to update.
   */
  public void setStatus(Status status) {
    this.status = status;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

  public enum Status {
    READY, CLOSED
  }
}
