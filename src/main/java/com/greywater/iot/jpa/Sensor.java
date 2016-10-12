package com.greywater.iot.jpa;

import javax.persistence.*;

/**
 * Created by alexander on 10/13/16.
 */
@Entity
@Table(name="SENSOR_TABLE")
public class Sensor {

    @Id
    @Column(name = "ID")
    @GeneratedValue
    private long id;

    @Column(name = "TYPE")
    private String type = null;

    @ManyToOne()
    @JoinColumn(name="THING_ID")
    private long thingID;

    public long getThingID() {
        return thingID;
    }

    public void setThingID(long thingID) {
        this.thingID = thingID;
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
}
