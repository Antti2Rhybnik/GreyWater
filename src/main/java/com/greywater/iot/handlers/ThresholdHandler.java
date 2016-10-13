package com.greywater.iot.handlers;

import com.greywater.iot.jpa.Message;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alexander on 10/13/16.
 */
public class ThresholdHandler implements Runnable {
    ArrayList<Message> messages;
    static boolean shitHappened;

    double low = 9;
    double max = 11;

    public ThresholdHandler(List<Message> messages) {
        this.messages = new ArrayList<>(messages);
        this.shitHappened = false;
    }

    @Override
    public void run() {
        for(Message message : messages) {
            if (message.getSensorValue() < low || message.getSensorValue() > max) {
                shitHappened = true;
                break;
            }
        }
    }
}
