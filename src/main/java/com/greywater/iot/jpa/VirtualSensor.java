package com.greywater.iot.jpa;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by alexander on 10/10/16.
 */
@Entity
@Table(name = "VIRTUAL_SENSORS", catalog = "")
@NamedQuery(name = "getAll", query = "SELECT v from VirtualSensor v")
@XmlRootElement
public class VirtualSensor implements Serializable{
    @TableGenerator(name = "CustomerGenerator", table = "ESPM_ID_GENERATOR", pkColumnName = "GENERATOR_NAME", valueColumnName = "GENERATOR_VALUE", pkColumnValue = "Customer", initialValue = 100000000, allocationSize = 100)
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "VirtualSensorGenerator")
    @Column(name = "VIRTUAL_SENSOR_ID", length = 10)
    private String  virtualSensorId;

    private String gDevice;

    public void setgDevice(String gDevice) {
        this.gDevice = gDevice;
    }

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

    @Override
    public String toString() {
        return "VirtualSensor{" +
                "virtualSensorId='" + virtualSensorId + '\'' +
                ", gDevice='" + gDevice + '\'' +
                '}';
    }
}
