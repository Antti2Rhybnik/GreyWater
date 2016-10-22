package com.greywater.iot.jpa;


import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="SENSORS_TABLE")
@NamedQueries({
        @NamedQuery(name = "Sensor.getAll", query = "SELECT s from Sensor s"),
        @NamedQuery(name = "Sensor.getByID", query = "SELECT s from Sensor s where s.id = :id")
})
@XmlRootElement
public class Sensor {

    @Id
    @Column(name = "SENSOR_ID")
    @GeneratedValue
    private Integer id;

    @Column(name = "TYPE")
    private String type;

    // TODO: возможно, штуку стоит перекинуть на виртуальные сенсоры
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "THING_ID")
    private Thing thing;

    // список виртуальных сенсоров, в которых данный сенсор участвует
    @ManyToMany(mappedBy = "sensors", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<VSensor> vsensors = new ArrayList<>();

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


    //////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static List<Sensor> getAll(){
        EntityManager entityManager = Persistence.createEntityManagerFactory("GreyWater").createEntityManager();
        List<Sensor> sensorEntities = entityManager.createNamedQuery("Sensor.getAll", Sensor.class).getResultList();
        entityManager.close();
        return sensorEntities;
    }

    public static Sensor getByID(Long id) {
        EntityManager entityManager = Persistence.createEntityManagerFactory("GreyWater").createEntityManager();
        TypedQuery<Sensor> query = entityManager.createNamedQuery("Sensor.getByID", Sensor.class);
        Sensor sensorEntity = query.getSingleResult();
        entityManager.close();
        return sensorEntity;
    }


    // TODO: fix this
//    public List<Message> getLast(int limit) {
//        EntityManager em = Persistence.createEntityManagerFactory("GreyWater").createEntityManager();
//        TypedQuery<Message> q = em.createNamedQuery("Message.lastN", Message.class);
//        q.setParameter("1", id);
//        q.setParameter("2", limit);
//        List<Message> list = q.getResultList();
//        em.close();
//        return list;
//    }
}
