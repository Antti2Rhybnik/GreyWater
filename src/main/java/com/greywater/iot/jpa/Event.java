package com.greywater.iot.jpa;

import com.greywater.iot.persistence.PersistManager;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by alexander on 31.10.16.
 */
@Entity
@Table(name="EVENTS_TABLE", schema = "NEO_77I8IO0F4PQ8TZ67A28RD0L2L", catalog = "")
@NamedNativeQueries({
        @NamedNativeQuery(name = "Event.getLastNEvents", query = "SELECT * FROM NEO_77I8IO0F4PQ8TZ67A28RD0L2L.EVENTS_TABLE  ORDER BY CREATED DESC LIMIT ?", resultClass = Event.class)
})
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



    public Event() {}

    public Event(Date created, String importanceRank, String eventType, double difference) {
        this.created = created;
        this.importanceRank = importanceRank;
        this.eventType = eventType;
        this.difference = difference;
    }

    public static List<Event> getLastNEvents (String vsensorID, int limit) {
        EntityManager em = PersistManager.newEntityManager();
        TypedQuery<Event> query = em.createNamedQuery("Event.getLastNEvents", Event.class);
        query.setParameter("1", limit);
        List<Event> list = query.getResultList();
        em.close();
        return list;
    }

    public String getId() {
        return id;
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
