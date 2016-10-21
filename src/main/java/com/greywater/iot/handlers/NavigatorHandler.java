package com.greywater.iot.handlers;

import com.greywater.iot.jpa.Message;
import com.greywater.iot.jpa.Sensor;

import java.util.List;

/**
 * Created by alexander on 10/13/16.
 */

/*Первый обработчик на который поступят сообщения
* Распределяет сообщения по следующим обработчикам*/
public class NavigatorHandler implements Runnable {

    List<Message> messages;

    public NavigatorHandler(List<Message> messages) {
        this.messages = messages;
    }

    @Override
    public void run() {
        List<Sensor> sensors = Sensor.getAll();

        HandlerScheduler.getHandlerExecutor().execute(new ThresholdHandler(messages));

    }
}
