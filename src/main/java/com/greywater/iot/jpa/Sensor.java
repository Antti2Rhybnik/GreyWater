package com.greywater.iot.jpa;

import javax.persistence.*;
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

    public Thing getThing() {
        return thing;
    }

    public void setThing(Thing thing) {
        this.thing = thing;
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
