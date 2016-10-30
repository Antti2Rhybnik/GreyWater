package com.greywater.iot.gwcontext;

import com.greywater.iot.jpa.Message;

import javax.naming.NamingException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.concurrent.Future;

/*Наблюдатель за таблицей сообщений
* С определенной периодичностью опрашивает таблицу сообщений
* и если появились новые передает их в MessageDistributor*/

public class Observer implements Runnable {

    private static Timestamp lastMsgTime = Message.getLastTime();
    private static Future future;
    private static int magicNumberCount = 0;

    Observer() {}

    @Override
    public void run() {

        try {

            System.out.println("observer start - " + lastMsgTime);

            List<Message> recentlyAddedMessages = Message.getAfterTime(lastMsgTime);
            if (!recentlyAddedMessages.isEmpty()) {

                System.out.println(recentlyAddedMessages.size());
                future = GWContext.getMsgDistribExecutor().submit(new MessageDistributor(recentlyAddedMessages));

                int lastIndex = recentlyAddedMessages.size() - 1;
                lastMsgTime = new Timestamp(recentlyAddedMessages.get(lastIndex).getgCreated().getTime());
            }

            System.out.println("observer end - " + lastMsgTime);

        } catch (SQLException | NamingException ex) {
            ex.printStackTrace();
        }
    }

}



