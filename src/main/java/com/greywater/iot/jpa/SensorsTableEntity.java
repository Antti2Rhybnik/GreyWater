package com.greywater.iot.jpa;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Created by antti on 08.10.16.
 */

@Entity
@Table(name = "SENSORS_TABLE", schema = "NEO_77I8IO0F4PQ8TZ67A28RD0L2L", catalog = "")
@NamedQuery(name = "getAll", query = "SELECT s from SensorsTableEntity s")
@XmlRootElement
public class SensorsTableEntity implements Serializable {
    private String gDevice;
    private Timestamp gCreated;
    private Timestamp timestamp;
    private String sensorId;
    private String sensorClass;
    private Double sensorValue;


    public SensorsTableEntity() {}

    @Basic
    @Column(name = "G_DEVICE")
    public String getgDevice() {
        return gDevice;
    }

    public void setgDevice(String gDevice) {
        this.gDevice = gDevice;
    }

    @Basic
    @Column(name = "G_CREATED")
    public Timestamp getgCreated() {
        return gCreated;
    }

    public void setgCreated(Timestamp gCreated) {
        this.gCreated = gCreated;
    }

    @Basic
    @Column(name = "TIMESTAMP")
    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    @Id
    @Column(name = "SENSOR_ID", unique = true)
    public String getSensorId() {
        return sensorId;
    }

    public void setSensorId(String sensorId) {
        this.sensorId = sensorId;
    }

    @Basic
    @Column(name = "SENSOR_CLASS")
    public String getSensorClass() {
        return sensorClass;
    }

    public void setSensorClass(String sensorClass) {
        this.sensorClass = sensorClass;
    }

    @Basic
    @Column(name = "SENSOR_VALUE")
    public Double getSensorValue() {
        return sensorValue;
    }

    public void setSensorValue(Double sensorValue) {
        this.sensorValue = sensorValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SensorsTableEntity that = (SensorsTableEntity) o;

        if (gDevice != null ? !gDevice.equals(that.gDevice) : that.gDevice != null) return false;
        if (gCreated != null ? !gCreated.equals(that.gCreated) : that.gCreated != null) return false;
        if (timestamp != null ? !timestamp.equals(that.timestamp) : that.timestamp != null) return false;
        if (sensorId != null ? !sensorId.equals(that.sensorId) : that.sensorId != null) return false;
        if (sensorClass != null ? !sensorClass.equals(that.sensorClass) : that.sensorClass != null) return false;
        if (sensorValue != null ? !sensorValue.equals(that.sensorValue) : that.sensorValue != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = gDevice != null ? gDevice.hashCode() : 0;
        result = 31 * result + (gCreated != null ? gCreated.hashCode() : 0);
        result = 31 * result + (timestamp != null ? timestamp.hashCode() : 0);
        result = 31 * result + (sensorId != null ? sensorId.hashCode() : 0);
        result = 31 * result + (sensorClass != null ? sensorClass.hashCode() : 0);
        result = 31 * result + (sensorValue != null ? sensorValue.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "SensorsTableEntity{" +
                "gDevice='" + gDevice + '\'' +
                ", gCreated=" + gCreated +
                ", timestamp=" + timestamp +
                ", sensorId='" + sensorId + '\'' +
                ", sensorClass='" + sensorClass + '\'' +
                ", sensorValue=" + sensorValue +
                '}';
    }
}
