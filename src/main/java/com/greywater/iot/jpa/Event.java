package com.greywater.iot.jpa;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

/**
 * Created by alexander on 31.10.16.
 */
@Entity
@Table(name="EVENTS_TABLE", schema = "NEO_77I8IO0F4PQ8TZ67A28RD0L2L", catalog = "")
public class Event implements Serializable {
    public static final String LOW_RANK = "LOW";
    public static final String MEDIUM_RANK = "MEDIUM";
    public static final String HIGH_RANK = "HIGH";

    public static final String THRESHOLD_TYPE = "THRESHOLD";

    @Id
    @Column(name = "EVENT_ID")
    private String id= UUID.randomUUID().toString();

    @Column(name = "GEN_TIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date created;


    //Метка важности события. может принимать значения LOW, MEDIUM, HIGH
    @Column(name = "IMPORTANCE_RANK")
    private String importanceRank;

    //Тип ивента. Вопрос на обсуждении, какие типы будут.
    //Threshold - нарушение граничных параметров
    @Column(name = "EVENT_TYPE")
    private String eventType;


    //Насколько сильно значение датчика перешло границу. Разница между границей и текущим значением датчика.
    @Column(name="DIFF")
    private double difference;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "VSENSOR_ID")
    private VirtualSensor virtualSensor;

    public Event() {}

    public Event(Date created, String importanceRank, String eventType, double difference, VirtualSensor virtualSensor) {
        this.created = created;
        this.importanceRank = importanceRank;
        this.eventType = eventType;
        this.difference = difference;
        this.virtualSensor = virtualSensor;
    }

    public String getId() {
        return id;
    }

    public VirtualSensor getVirtualSensor() {
        return virtualSensor;
    }

    public void setVirtualSensor(VirtualSensor virtualSensor) {
        this.virtualSensor = virtualSensor;
    }

    public Date getCreated() {

        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public String getImportanceRank() {
        return importanceRank;
    }

    public void setImportanceRank(String importanceRank) {
        this.importanceRank = importanceRank;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public double getDifference() {
        return difference;
    }

    public void setDifference(double difference) {
        this.difference = difference;
    }

    public void setId(String id) {
        this.id = id;
    }
}
