package com.greywater.iot.vsensors;


import com.greywater.iot.jpa.VirtualSensor;

public abstract class VirtualSensorAggregator {

    VirtualSensor vs;

    public VirtualSensorAggregator(VirtualSensor vs) {
        this.vs = vs;
    }

    public abstract Double eval();

}
