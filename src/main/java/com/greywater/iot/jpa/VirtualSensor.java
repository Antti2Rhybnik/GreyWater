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
    @Column(name = "G_CREATED")
    private Date gCreated = null;



    @Column(name = "G_DEVICE")
    private String gDevice;


    @PrePersist
    protected void generateInitialData(){
        final Date now = new Date();
        gCreated = now;
    }


    public String getgDevice() {
        return gDevice;
    }

    public void setgDevice(String gDevice) {
        this.gDevice = gDevice;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getgCreated() {
        return gCreated;
    }

    public void setgCreated(Date gCreated) {
        this.gCreated = gCreated;
    }
}
