package com.greywater.iot.handlers;

import com.greywater.iot.jpa.Message;
import com.greywater.iot.jpa.Sensor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alexander on 10/13/16.
 */
public class NavigatorHandler implements Runnable {

    List<Message> messages;

    public NavigatorHandler(List<Message> messages) {
        this.messages = messages;
    }

    @Override
    public void run() {
        ArrayList<Sensor> sensors = new ArrayList<>(Sensor.getAllSensors());
        HandlerScheduler.getHandlerExecutor().execute(new ThresholdHandler(messages));
        messages.forEach( it->{
            // Здесь будут формироваться контейнеры для специфических обработчиков, где не нужны ВСЕ пришедшие сообщения.
        });
    }
}
