package com.greywater.iot.gwcontext;

import com.greywater.iot.gwcontext.GWContext;
import com.greywater.iot.jpa.Message;
import com.greywater.iot.jpa.Sensor;
import com.greywater.iot.jpa.VirtualSensor;

import java.util.List;
import java.util.Optional;


public class MessageDistributor implements Runnable {

    List<Message> messages;

    public MessageDistributor(List<Message> messages) {
        this.messages = messages;
    }

    @Override
    public void run() {

        System.out.println("message distributor is running");
        for (Message m: messages) {

            Optional<Sensor> matchedSensor = Optional.empty();

            for (Sensor s : GWContext.getAllSensors()) {
                if (s.getId().equals(m.getSensorId())) {
                    matchedSensor = Optional.of(s);
                    break;
                }
            }

            if (matchedSensor.isPresent()) {
                matchedSensor.get().updateActualMessage(m);
                matchedSensor.get().getVirtualSensors().forEach(VirtualSensor::eval);
            }

        };
        System.out.println("message distributor is off");

    }
}
