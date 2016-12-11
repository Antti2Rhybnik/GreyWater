package com.greywater.iot.gwcontext;

import com.greywater.iot.jpa.Message;

import java.util.List;
import java.util.Optional;


public class MessageDistributor implements Runnable {

    List<Message> messages;

    public MessageDistributor(List<Message> messages) {
        this.messages = messages;
    }

    @Override
    public void run() {

        System.out.println("message distributor is running");

        System.out.println("message distributor is off");

    }
}
