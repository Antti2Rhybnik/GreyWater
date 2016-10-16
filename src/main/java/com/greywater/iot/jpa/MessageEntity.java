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
    @NamedQuery(name = "Message.getAll", query = "SELECT s from MessageEntity s"),
    @NamedQuery(name = "Message.getLast", query = "SELECT s from MessageEntity s where s.gCreated > :timestamp"),
})
@NamedNativeQueries({
//    @NamedNativeQuery(name = "MessageEntity.lastActualValues", query = "SELECT SENSOR_VALUE FROM MESSAGES_TABLE WHERE SENSOR_ID = ? ORDER BY G_CREATED DESC LIMIT ?", resultClass = Double.class),
//    @NamedNativeQuery(name = "MessageEntity.lastActualTimestamps", query = "SELECT G_CREATED FROM MESSAGES_TABLE WHERE SENSOR_ID = ? ORDER BY G_CREATED DESC LIMIT ?", resultClass = Date.class),
    @NamedNativeQuery(name = "MessageEntity.lastActualMessages", query = "SELECT * FROM MESSAGES_TABLE WHERE SENSOR_ID = ? ORDER BY G_CREATED DESC LIMIT ?", resultClass = MessageEntity.class)
})
@XmlRootElement
public class MessageEntity implements Serializable {

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

    public MessageEntity() {}

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


    public static List<MessageEntity> getLastMessages(Timestamp t) {
        EntityManager em = Persistence.createEntityManagerFactory("GreyWater").createEntityManager();
        TypedQuery<MessageEntity> query = em.createNamedQuery("Message.getLast", MessageEntity.class);
        query.setParameter("timestamp", t, TemporalType.TIMESTAMP);
        List<MessageEntity> list = query.getResultList();
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
