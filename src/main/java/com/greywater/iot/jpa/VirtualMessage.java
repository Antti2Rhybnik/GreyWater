package com.greywater.iot.jpa;


import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;
import java.util.UUID;


@Entity
@Table(name = "VIRTUAL_MESSAGES_TABLE")
@XmlRootElement
public class VirtualMessage {

    // === FIELDS === //
    @Id
    @Column(name = "VMESSAGE_ID")
    private String id = UUID.randomUUID().toString();

    @Column(name = "VALUE")
    private Double value;

    @Column(name = "CREATED")
    @Temporal(TemporalType.TIMESTAMP)
    private Date created;

    @ManyToOne
    @JoinColumn(name = "VSENSOR_ID")
    private VirtualSensor virtualSensor;


    // === GETTERS AND SETTERS === //
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public VirtualSensor getVirtualSensor() {
        return virtualSensor;
    }

    public void setVirtualSensor(VirtualSensor virtualSensor) {
        this.virtualSensor = virtualSensor;
    }
}
