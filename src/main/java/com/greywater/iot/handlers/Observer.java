package com.greywater.iot.handlers;

import com.greywater.iot.jpa.Message;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

/*Наблюдатель за таблицей сообщений
* С определенной периодичностью опрашивает таблицу сообщений
* и если появились новые передает их в MessageDistributor*/

public class Observer implements Runnable {

    private static Timestamp lastMsgTime;

    Observer() {
        // TODO: неясно пока, что делать в случае, когда запуск не в первый раз
        lastMsgTime = new Timestamp(0);
    }

    @Override
    public void run() {

        System.out.println("I'm in observer - " + Instant.now());

        List<Message> recentlyAddedMessages = Message.getAfterTime(lastMsgTime);
        if (!recentlyAddedMessages.isEmpty()) {

            System.out.println(recentlyAddedMessages.size());
            GWContext.getMsgDistribExecutor().execute(new MessageDistributor(recentlyAddedMessages));

            int lastIndex = recentlyAddedMessages.size() - 1;
            lastMsgTime = new Timestamp(recentlyAddedMessages.get(lastIndex).getgCreated().getTime());
        }

    }

}



