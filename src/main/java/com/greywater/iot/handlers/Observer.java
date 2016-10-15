package com.greywater.iot.handlers;

import com.greywater.iot.jpa.Message;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by alexander on 10/13/16.
 */
public class Observer implements Runnable {
    public static Timestamp recentlyActiveDate;
    public static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

    public Observer() {

        recentlyActiveDate = Observer.getCurrentTimestamp();
//        cal.add(Calendar.DATE, -1);
    }

    @Override
    public void run() {
        Calendar cal = Calendar.getInstance();
//        cal.add(Calendar.DATE, -1);

        System.out.println("I'm in observer - " + Instant.now());

        List<Message> recentlyAddedMessages = Message.getLastMessages(recentlyActiveDate);
        if (!recentlyAddedMessages.isEmpty()) {

            System.out.println(recentlyAddedMessages.size());
            HandlerScheduler.getHandlerExecutor().execute(new NavigatorHandler(recentlyAddedMessages));
        }


        recentlyActiveDate = Observer.getCurrentTimestamp();
//        recentlyActiveDate = cal.getTime();
    }

    public static Timestamp getCurrentTimestamp() {
        try {
            Date  parsedDate = dateFormat.parse(Instant.now().toString());
            return new Timestamp(parsedDate.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
            return new Timestamp(0);
        }
    }
}



