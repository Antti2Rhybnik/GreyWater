package com.greywater.iot.vsensors;

import com.greywater.iot.jpa.Sensor;

import java.util.List;

public class SimpleRedirect extends VSensorDelegate {

    public SimpleRedirect(List<Sensor> sensors) {
        super(sensors);
    }

    @Override
    public Double eval() {
//        return sensors.get(0).getValue(); // TODO: need some magic
        return 0d;
    }
}
