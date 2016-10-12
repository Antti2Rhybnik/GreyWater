package com.greywater.iot.jpa;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

/**
 * Created by alexander on 10/12/16.
 */
@MappedSuperclass
public abstract class VirtualSensor implements VirtualSensorEvaluable {

    @Id
    @Column(name = "ID")
    private String id = UUID.randomUUID().toString();

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "INCOME_DATE")
    private Date camedAt = null;

    @PrePersist
    protected void generateInitialData(){
        final Date now = new Date();
        camedAt = now;
    }





    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getCamedAt() {
        return camedAt;
    }

    public void setCamedAt(Date camedAt) {
        this.camedAt = camedAt;
    }
}
