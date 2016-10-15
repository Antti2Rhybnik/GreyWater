package com.greywater.iot.handlers;

import com.greywater.iot.jpa.Message;
import com.greywater.iot.jpa.Parameters;
import com.greywater.iot.jpa.Sensor;

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
        List<Sensor> sensors = Sensor.getAll();

        sensors.forEach(s -> {

            List<Message> messages = s.getLast(30);
            Parameters p = s.getParameters();

        });

//        HandlerScheduler.getHandlerExecutor().execute(new ThresholdHandler(messages));
        messages.forEach(msg -> {
//             Здесь будут формироваться контейнеры для специфических обработчиков, где не нужны ВСЕ пришедшие сообщения.
            Parameters p = Sensor.getByID(msg.getSensorId()).getParameters();
//             и что мы тут должны делать по твоему видению?
            if (p.getMax() != null) {
                if (msg.getSensorValue() > p.getMax()) {
                    ThresholdHandler.problemDetected = true;
                    // параметр MAX для сенсора с ID = :id - наэбнулся
                }
            }
            if (p.getMin() != null) {
                if (msg.getSensorValue() < p.getMin()) {
                    ThresholdHandler.problemDetected = true;
                }
            }

        });
    }
}
