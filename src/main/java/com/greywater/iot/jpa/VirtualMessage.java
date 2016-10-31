package com.greywater.iot.jpa;


import com.greywater.iot.persistence.PersistManager;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;
import java.util.List;
import java.util.UUID;


@Entity
@Table(name = "VIRTUAL_MESSAGES_TABLE")
@NamedNativeQueries({
        @NamedNativeQuery(name = "VirtualMessage.getLastNMessages", query = "SELECT * FROM VIRTUAL_MESSAGES_TABLE WHERE VSENSOR_ID = ? ORDER BY CREATED DESC LIMIT ?", resultClass = VirtualMessage.class)
})
@XmlRootElement
public class VirtualMessage {

    // === FIELDS === //
    @Id
    @Column(name = "VMESSAGE_ID")
    private String id = UUID.randomUUID().toString();

    @Column(name = "VALUE")
    private Double value;

    @Column(name = "TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date timestamp;

    @ManyToOne
    @JoinColumn(name = "VSENSOR_ID")
    private VirtualSensor virtualSensor;


    // === GETTERS AND SETTERS === //

    public static List<VirtualMessage> getLastNMessages(String vsensorID, int limit) {
        EntityManager em = PersistManager.newEntityManager();
        TypedQuery<VirtualMessage> query = em.createNamedQuery("VirtualMessage.getLastNMessages", VirtualMessage.class);
        query.setParameter("1", vsensorID);
        query.setParameter("2", limit);
        List<VirtualMessage> list = query.getResultList();
        em.close();
        return list;
    }


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

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date created) {
        this.timestamp = created;
    }

    public VirtualSensor getVirtualSensor() {
        return virtualSensor;
    }

    public void setVirtualSensor(VirtualSensor virtualSensor) {
        this.virtualSensor = virtualSensor;
    }
}
