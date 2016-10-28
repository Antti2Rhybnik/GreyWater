package com.greywater.iot.vsensors;


import com.greywater.iot.jpa.VirtualSensor;

public class Multiplicator extends VirtualSensorAggregator {

    public Multiplicator(VirtualSensor vs) {
        super(vs);
    }

    @Override
    public Double eval() {

        return vs.getSensors().stream()
                .mapToDouble(s -> s.getActualMessage().getSensorValue())
                .reduce(1, (a, b) -> a * b);
    }
}
