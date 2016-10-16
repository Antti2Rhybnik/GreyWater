package com.greywater.iot.jpa;


import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="SENSORS_TABLE")
@NamedQueries({
        @NamedQuery(name = "Sensor.getAll", query = "SELECT s from SensorEntity s"),
        @NamedQuery(name = "Sensor.getByID", query = "SELECT s from SensorEntity s where s.id = :id")
})
@XmlRootElement
public class SensorEntity {

    @Id
    @Column(name = "ID")
    @GeneratedValue
    private Integer id;

    @Column(name = "TYPE")
    private String type;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "THING_ID")
    private Thing thing;

    @OneToMany(mappedBy = "sensorEntity", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<PredicateEntity> predicateEntities = new ArrayList<>();;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Thing getThing() {
        return thing;
    }

    public void setThing(Thing thing) {
        this.thing = thing;
    }

    public List<PredicateEntity> getPredicateEntities() {
        return predicateEntities;
    }

    public void setPredicateEntities(List<PredicateEntity> predicateEntities) {
        this.predicateEntities = predicateEntities;
    }

    public void addPredicateEntity(PredicateEntity predicateEntity) {
        if (predicateEntity.getSensorEntityEntity() != this) {
            this.predicateEntities.add(predicateEntity);
            predicateEntity.setSensorEntityEntity(this);
        }
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static List<SensorEntity> getAll(){
        EntityManager entityManager = Persistence.createEntityManagerFactory("GreyWater").createEntityManager();
        List<SensorEntity> sensorEntityEntities = entityManager.createNamedQuery("Sensor.getAll", SensorEntity.class).getResultList();
        entityManager.close();
        return sensorEntityEntities;
    }

    public static SensorEntity getByID(Long id) {
        EntityManager entityManager = Persistence.createEntityManagerFactory("GreyWater").createEntityManager();
        TypedQuery<SensorEntity> query = entityManager.createNamedQuery("Sensor.getByID", SensorEntity.class);
        SensorEntity sensorEntityEntity = query.getSingleResult();
        entityManager.close();
        return sensorEntityEntity;
    }

    public List<MessageEntity> getLast(int limit) {
        EntityManager em = Persistence.createEntityManagerFactory("GreyWater").createEntityManager();
        TypedQuery<MessageEntity> q = em.createNamedQuery("Message.lastN", MessageEntity.class);
        q.setParameter("1", id);
        q.setParameter("2", limit);
        List<MessageEntity> list = q.getResultList();
        em.close();
        return list;
    }
}
