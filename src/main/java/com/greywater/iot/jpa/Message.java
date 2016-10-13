package com.greywater.iot.jpa;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Created by antti on 08.10.16.
 */

@Entity
@Table(name = "MESSAGES_TABLE", schema = "NEO_77I8IO0F4PQ8TZ67A28RD0L2L", catalog = "")
@NamedQuery(name = "getAll", query = "SELECT s from Message s")
@XmlRootElement
public class Message implements Serializable {
    private String gDevice;
    private Timestamp gCreated;
    private Long sensorId;
    private Double sensorValue;


    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "SENSOR_ID")
    Sensor sensor;


    public Message() {}


    @Column(name = "G_DEVICE")
    public String getgDevice() {
        return gDevice;
    }

    public void setgDevice(String gDevice) {
        this.gDevice = gDevice;
    }

    @Id
    @Column(name = "G_CREATED")
    public Timestamp getgCreated() {
        return gCreated;
    }

    public void setgCreated(Timestamp gCreated) {
        this.gCreated = gCreated;
    }


    @Column(name = "SENSOR_ID")
    public Long getSensorId() {
        return sensorId;
    }

    public void setSensorId(Long sensorId) {
        this.sensorId = sensorId;
    }


    @Column(name = "SENSOR_VALUE")
    public Double getSensorValue() {
        return sensorValue;
    }

    public void setSensorValue(Double sensorValue) {
        this.sensorValue = sensorValue;
    }


    @Override
    public String toString() {
        return "Message{" +
                "gDevice='" + gDevice + '\'' +
                ", gCreated=" + gCreated +
                ", sensorId='" + sensorId + '\'' +
                ", sensorValue=" + sensorValue +
                '}';
    }
}
