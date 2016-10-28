package com.greywater.iot.vsensors;


import com.greywater.iot.jpa.VirtualSensor;

public class SimpleRedirector extends VirtualSensorAggregator {


    public SimpleRedirector(VirtualSensor vs) {
        super(vs);
    }

    @Override
    public Double eval() throws SensorNullMessageException {
        checkNullMessage();
        return vs.getSensors().get(0).getActualMessage().getSensorValue();
    }
}
