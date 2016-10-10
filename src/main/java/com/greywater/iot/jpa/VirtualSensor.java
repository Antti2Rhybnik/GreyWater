package com.greywater.iot.jpa;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by alexander on 10/10/16.
 */
@Entity
@Table(name = "VIRTUAL_SENSORS", schema="NEO_77I8IO0F4PQ8TZ67A28RD0L2L", catalog = "")
@NamedQuery(name = "getAll", query = "SELECT v from VIRTUAL_SENSORS v")
@XmlRootElement
public class VirtualSensor {
    @TableGenerator(name = "CustomerGenerator", table = "ESPM_ID_GENERATOR", pkColumnName = "GENERATOR_NAME", valueColumnName = "GENERATOR_VALUE", pkColumnValue = "Customer", initialValue = 100000000, allocationSize = 100)
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "VirtualSensorGenerator")
    @Column(name = "VIRTUAL_SENSOR_ID", length = 10)
    private String  virtualSensorId;

    private String gDevice;
    private Timestamp gCreated;
    private Timestamp timestamp;
    private String sensorId;
    private String sensorClass;
    private Double sensorValue;

    public String getVirtualSensorId() {
        return virtualSensorId;
    }

    public void setVirtualSensorId(String virtualSensorId) {
        this.virtualSensorId = virtualSensorId;
    }

    @Basic
    @Column(name = "G_DEVICE")
    public String getgDevice() {
        return gDevice;
    }

    public void setgDevice(String gDevice) {
        this.gDevice = gDevice;
    }

    public Timestamp getgCreated() {
        return gCreated;
    }

    public void setgCreated(Timestamp gCreated) {
        this.gCreated = gCreated;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public String getSensorId() {
        return sensorId;
    }

    public void setSensorId(String sensorId) {
        this.sensorId = sensorId;
    }

    public String getSensorClass() {
        return sensorClass;
    }

    public void setSensorClass(String sensorClass) {
        this.sensorClass = sensorClass;
    }

    public Double getSensorValue() {
        return sensorValue;
    }

    public void setSensorValue(Double sensorValue) {
        this.sensorValue = sensorValue;
    }

    @Override
    public String toString() {
        return "VirtualSensor{" +
                "virtualSensorId='" + virtualSensorId + '\'' +
                ", gDevice='" + gDevice + '\'' +
                ", gCreated=" + gCreated +
                ", timestamp=" + timestamp +
                ", sensorId='" + sensorId + '\'' +
                ", sensorClass='" + sensorClass + '\'' +
                ", sensorValue=" + sensorValue +
                '}';
    }
}
