package com.greywater.iot.jpa;

import javax.persistence.*;
import javax.xml.stream.StreamFilter;
import java.util.Date;
import java.util.UUID;

/**
 * Created by alexander on 10/12/16.
 */
@MappedSuperclass
public abstract class VirtualSensor {

    @Id
    @Column(name = "ID")
    private String id = UUID.randomUUID().toString();

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "INCOME_DATE")
    private Date incomedAt = null;

    @PrePersist
    protected void generateInitialData(){
        final Date now = new Date();
        incomedAt = now;
    }


    //Add sensor value to virtual sensor and then evaluate virtual sensor logic
    protected void addSensorValue(SensorsTableEntity sensorsTableEntity){}

    //evaluation method for virtual sensor
    protected void computeVirtualSensorValue(){}


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getIncomedAt() {
        return incomedAt;
    }

    public void setIncomedAt(Date incomedAt) {
        this.incomedAt = incomedAt;
    }
}
