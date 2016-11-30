package com.greywater.iot.nodeNetwork;

import com.greywater.iot.jpa.Message;

import java.util.List;

public class SensorNode extends Node<Double> {

    SensorNode() {
        super();
    }

    SensorNode(List<Node> inputs) {
        super(inputs);
    }

    void eval() {

        for (Message m : Message.lastMessages) {
            if (m.getSensorId().equals(id)) {
                state = m.getSensorValue();
                break;
            }
        }

    }

}
