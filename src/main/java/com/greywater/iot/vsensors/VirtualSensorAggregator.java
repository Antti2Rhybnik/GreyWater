package com.greywater.iot.vsensors;


import com.greywater.iot.jpa.Sensor;
import com.greywater.iot.jpa.VirtualSensor;

public abstract class VirtualSensorAggregator {

    VirtualSensor vs;

    VirtualSensorAggregator(VirtualSensor vs) {
        this.vs = vs;
    }

    void checkNullMessage() throws SensorNullMessageException {

        for(Sensor s: vs.getSensors()) {
            if (s.getActualMessage() == null) throw new SensorNullMessageException("Sensor@{"+s.getId()+"} still has no actual messages");
        }
    }

    public abstract Double eval() throws SensorNullMessageException;

}
