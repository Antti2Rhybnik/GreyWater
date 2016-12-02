package com.greywater.iot.nodeNetwork;

import com.greywater.iot.jpa.Message;

import java.util.List;

public class SensorNode extends Node<Double> {

    Long sensorId;

    public SensorNode() {
        super();
    }


    SensorNode(List<Node> inputs) {
        super(inputs);
    }

    void eval() {

        for (Message m : Message.lastMessages) {
            if (m.getSensorId().equals(sensorId)) {
                state = m.getSensorValue();
                break;
            }
        }

    }


    public Long getSensorId() {
        return sensorId;
    }

    public void setSensorId(Long sensorId) {
        this.sensorId = sensorId;
    }

}
