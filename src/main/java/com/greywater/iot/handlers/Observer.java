package com.greywater.iot.handlers;

import com.greywater.iot.jpa.Message;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by alexander on 10/13/16.
 */
public class Observer implements Runnable {
    public static Date recentlyActiveDate = new Date();

    public Observer() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        recentlyActiveDate = cal.getTime();
    }

    @Override
    public void run() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        System.out.println("I'm in observer");

        List<Message> recentlyAddedMessages = Message.getLastMessages(recentlyActiveDate);
        if (!recentlyAddedMessages.isEmpty()) {
            HandlerScheduler.getHandlerExecutor().execute(new BottleWaterHandler(recentlyAddedMessages,1));
        }
        recentlyActiveDate = cal.getTime();

    }
}
