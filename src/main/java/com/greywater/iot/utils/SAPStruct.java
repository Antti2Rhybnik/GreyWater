package com.greywater.iot.utils;


import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;


@XmlRootElement
class Message {
    public Long sensor_id;

    @Override
    public String toString() {
        return "Message{sensor_id=" + sensor_id + '}';
    }
}


@XmlRootElement
public class SAPStruct {

    public List<Message> messages;

    SAPStruct() {}

    @Override
    public String toString() {
        return messages.get(0).toString();
    }

    public Long getSensorID() {
        return messages.get(0).sensor_id;
    }


}
