package com.greywater.iot.vsensors;

import com.greywater.iot.jpa.Sensor;

import java.util.List;

public abstract class VSensorDelegate {

    List<Sensor> sensors;

    public VSensorDelegate(List<Sensor> sensors) {
        this.sensors = sensors;
    }

    public abstract Double eval();

}
