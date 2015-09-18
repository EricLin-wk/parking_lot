package com.parking.entity;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Abstract class for Gates of the parking lot
 */
public abstract class Gate {

    private static int ID_GENERATOR = 0;

    private int gateId;

    private String name;

    protected Gate(String name) {
        this.gateId = ++ID_GENERATOR;
        this.name = name;
    }

    /**
     * Get this gate's auto generated Id.
     *
     * @return Gate Id.
     */
    public int getGateId() {
        return gateId;
    }

    /**
     * @return Gate name.
     */
    public String getName() {
        return name;
    }

    /**
     * Set this gate's name.
     *
     * @param name Gate name.
     */
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
