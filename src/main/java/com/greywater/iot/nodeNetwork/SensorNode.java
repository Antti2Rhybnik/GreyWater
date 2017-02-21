package com.greywater.iot.nodeNetwork;

import com.greywater.iot.jpa.Message;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement
public class SensorNode extends Node<Double> {

    Long sensorId;
    String sensorType;

    public SensorNode() {
        super();
        type = "sensor";
    }


    SensorNode(List<Node> inputs) {
        super(inputs);
    }

    void eval() {

        for (Message m : Message.lastMessages) {
            if (m.getSensorId().equals(sensorId)) {
                System.out.println(sensorId);
                state = m.getSensorValue();
                break;
            }
        }

        System.out.println("SensorNode " + id + ": " + state);

    }


    public Long getSensorId() {
        return sensorId;
    }

    public void setSensorId(Long sensorId) {
        this.sensorId = sensorId;
    }

    public String getSensorType() {
        return sensorType;
    }

    public void setSensorType(String sensorType) {
        this.sensorType = sensorType;
    }
}
