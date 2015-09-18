package com.parking.entity;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Class for exiting gates out of the parking lot.
 */
public class ExitGate extends Gate {

    public ExitGate(final String name) {
        super(name);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
