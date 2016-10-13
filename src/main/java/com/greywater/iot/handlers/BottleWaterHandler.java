package com.greywater.iot.handlers;

import com.greywater.iot.jpa.Message;

import java.util.List;

/**
 * Created by alexander on 10/13/16.
 */
public class BottleWaterHandler implements Runnable {
    List<Message> messages;
    int gDevice;
    double sum=0;
    int n=0;

    public BottleWaterHandler(List<Message> messages, int gDevice) {
        this.messages = messages;
        this.gDevice = gDevice;
    }

    @Override
    public void run() {
        messages.forEach(it->{
            if (it.getgDevice() == String.valueOf(gDevice)) {
                sum += it.getSensorValue();
                n++;
            }
        });
        System.out.println(sum );
    }
}
