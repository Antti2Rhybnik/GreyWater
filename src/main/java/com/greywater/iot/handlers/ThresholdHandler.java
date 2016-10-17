package com.greywater.iot.handlers;

import com.greywater.iot.jpa.Message;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alexander on 10/13/16.
 */
public class ThresholdHandler implements Runnable {
    ArrayList<Message> messages;
    static boolean problemDetected;


    static double low = 200;
    static double max = 1000;

    public ThresholdHandler(List<Message> messages) {
        this.messages = new ArrayList<>(messages);
        problemDetected = false;
    }

    @Override
    public void run() {
        for(Message message : messages) {
            if (message.getSensorValue() < low || message.getSensorValue() > max) {
                problemDetected = true;
            }
        }
    }

    public static boolean isProblemDetected() {
        return problemDetected;
    }

    public static void setProblemDetected(boolean problemDetected) {
        ThresholdHandler.problemDetected = problemDetected;
    }

    public static double getLow() {
        return low;
    }

    public static void setLow(double low) {
        ThresholdHandler.low = low;
    }

    public static double getMax() {
        return max;
    }

    public static void setMax(double max) {
        ThresholdHandler.max = max;
    }
}
