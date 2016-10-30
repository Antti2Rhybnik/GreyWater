package com.greywater.iot.handlers;

import com.greywater.iot.jpa.Message;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.concurrent.Future;

/*Наблюдатель за таблицей сообщений
* С определенной периодичностью опрашивает таблицу сообщений
* и если появились новые передает их в MessageDistributor*/

public class Observer implements Runnable {

    private static Timestamp lastMsgTime = new Timestamp(0); // FIXME
    private static Future future;
    private static int magicNumberCount = 0;

    Observer() {}

    @Override
    public void run() {

        try {

            System.out.println("observer start - " + magicNumberCount++);

            List<Message> recentlyAddedMessages = Message.getAfterTime(lastMsgTime);
            if (!recentlyAddedMessages.isEmpty()) {

                System.out.println(recentlyAddedMessages.size());
                future = GWContext.getMsgDistribExecutor().submit(new MessageDistributor(recentlyAddedMessages));

                int lastIndex = recentlyAddedMessages.size() - 1;
                lastMsgTime = new Timestamp(recentlyAddedMessages.get(lastIndex).getgCreated().getTime());
            }

            System.out.println("observer end - " + lastMsgTime);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}



