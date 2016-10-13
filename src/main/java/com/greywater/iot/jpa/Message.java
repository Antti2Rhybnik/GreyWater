package com.greywater.iot.jpa;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

/**
 * Created by antti on 08.10.16.
 */

@Entity
@Table(name = "MESSAGES_TABLE", schema = "NEO_77I8IO0F4PQ8TZ67A28RD0L2L", catalog = "")
@NamedQueries({
        @NamedQuery(name = "Message.getAll", query = "SELECT s from Message s"),
        @NamedQuery(name = "Message.getLast", query = "SELECT s from Message s where s.gCreated > :timestamp")
})
@XmlRootElement
public class Message implements Serializable {

    @Column(name = "G_DEVICE")
    private String gDevice;

    @Id
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "G_CREATED")
    private Date gCreated;

    @Column(name = "SENSOR_ID")
    private long sensorId;

    @Column(name = "SENSOR_VALUE")
    private Double sensorValue;

    public Message() {}

    public String getgDevice() {
        return gDevice;
    }

    public void setgDevice(String gDevice) {
        this.gDevice = gDevice;
    }


    public void setgCreated(Timestamp gCreated) {
        this.gCreated = gCreated;
    }

    public long getSensorId() {
        return sensorId;
    }

    public void setSensorId(long sensorId) {
        this.sensorId = sensorId;
    }

    public Double getSensorValue() {
        return sensorValue;
    }

    public void setSensorValue(Double sensorValue) {
        this.sensorValue = sensorValue;
    }


    public List<Message> getLastMessages(Date d) {
        EntityManager em = Persistence.createEntityManagerFactory("GreyWater").createEntityManager();
        TypedQuery<Message> query = em.createNamedQuery("Message.getLast", Message.class);
        query.setParameter("timestamp", d, TemporalType.TIMESTAMP);
        List<Message> list = query.getResultList();
        em.close();
        return list;
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
