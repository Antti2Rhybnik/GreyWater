package com.greywater.iot.jpa;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by alexander on 10/13/16.
 */
@Entity
@Table(name="SENSORS_TABLE")
public class Sensor {

    @Id
    @Column(name = "ID")
    @GeneratedValue
    private long id;

    @Column(name = "TYPE")
    private String type = null;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "THING_ID")
    private Thing thing;

    @OneToMany(mappedBy = "sensor", cascade = CascadeType.ALL)
    private List<Message> messages = new ArrayList<Message>();

    public Thing getThing() {
        return thing;
    }



    public void setThing(Thing thing)
    {
        this.thing = thing;
        if(!thing.getSensors().contains(this)){
            thing.getSensors().add(this);
        }
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }
}
