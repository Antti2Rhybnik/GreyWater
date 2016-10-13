package com.greywater.iot.jpa;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

/**
 * Created by antti on 08.10.16.
 */

@Entity
@Table(name = "MESSAGES_TABLE", schema = "NEO_77I8IO0F4PQ8TZ67A28RD0L2L", catalog = "")
@NamedQuery(name = "getAll", query = "SELECT s from Message s")
@XmlRootElement
public class Message implements Serializable {
    private String gDevice;

    @Id
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "G_CREATED")
    private Date gCreated;
    private String sensorId;
    private Double sensorValue;



    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "SENSOR_ID")
    private Sensor sensor;



    public Message() {}


    @Column(name = "G_DEVICE")
    public String getgDevice() {
        return gDevice;
    }

    public void setgDevice(String gDevice) {
        this.gDevice = gDevice;
    }




    public void setgCreated(Timestamp gCreated) {
        this.gCreated = gCreated;
    }


    @Column(name = "SENSOR_ID", unique = true)
    public String getSensorId() {
        return sensorId;
    }

    public void setSensorId(String sensorId) {
        this.sensorId = sensorId;
    }


    @Column(name = "SENSOR_VALUE")
    public Double getSensorValue() {
        return sensorValue;
    }

    public void setSensorValue(Double sensorValue) {
        this.sensorValue = sensorValue;
    }

    public Sensor getSensor() {
        return sensor;
    }

    public void setSensor(Sensor sensor) {
        this.sensor = sensor;
    }

    public Date getgCreated() {
        return gCreated;
    }

    public void setgCreated(Date gCreated) {
        this.gCreated = gCreated;
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
