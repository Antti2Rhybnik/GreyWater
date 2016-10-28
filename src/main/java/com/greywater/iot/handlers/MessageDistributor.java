package com.greywater.iot.handlers;

import com.greywater.iot.jpa.Message;
import com.greywater.iot.jpa.Sensor;
import com.greywater.iot.jpa.VirtualSensor;

import java.util.List;
import java.util.Optional;

/*Первый обработчик на который поступят сообщения
* Распределяет сообщения по следующим обработчикам*/
public class MessageDistributor implements Runnable {

    List<Message> messages;

    public MessageDistributor(List<Message> messages) {
        this.messages = messages;
    }

    @Override
    public void run() {

        for (Message m: messages) {

            Optional<Sensor> matchedSensor = GWContext.getAllSensors().stream()
                    .filter(s -> s.getId() == m.getSensorId())
                    .findFirst();

            if (matchedSensor.isPresent()) {

                matchedSensor.get().updateActualMessage(m);
                matchedSensor.get().getVirtualSensors().forEach(VirtualSensor::eval);

            }

        };

    }
}
