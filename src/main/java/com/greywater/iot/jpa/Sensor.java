package com.greywater.iot.jpa;

import javax.persistence.*;
import java.util.UUID;

/**
 * Created by alexander on 10/13/16.
 */
@Entity
@Table(name="SENSOR_TABLE")
public class Sensor {

    @Id
    @Column(name = "ID")
    private String id= null;

    @Column(name = "TYPE")
    private String type = null;

    @ManyToOne()
    @JoinColumn(name="THING_ID")
    private String thingID;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
