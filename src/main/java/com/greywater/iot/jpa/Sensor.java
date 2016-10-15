package com.greywater.iot.jpa;


import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
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
    @Column(name = "ID")
    @GeneratedValue
    private Long id;

    @Column(name = "TYPE")
    private String type = null;

    @ManyToOne( cascade = CascadeType.ALL)
    @JoinColumn(name = "THING_ID")
    private Thing thing;

    @OneToOne
    @JoinColumn(name = "PARAM_ID")
    private Parameters parameters;


    public Thing getThing() {
        return thing;
    }



    public void setThing(Thing thing)
    {
        this.thing = thing;
        if(!thing.getSensors().contains(this)){
            thing.getSensors().add(this);
        }
    }

    public static List<Sensor> getAll(){
        EntityManager entityManager = Persistence.createEntityManagerFactory("GreyWater").createEntityManager();
        List<Sensor> sensors = entityManager.createNamedQuery("Sensor.getAll", Sensor.class).getResultList();
        entityManager.close();
        return sensors;
    }

    public static Sensor getByID(Long id) {
        EntityManager entityManager = Persistence.createEntityManagerFactory("GreyWater").createEntityManager();
        TypedQuery<Sensor> query = entityManager.createNamedQuery("Sensor.getByID", Sensor.class);
        Sensor sensor = query.getSingleResult();
        entityManager.close();
        return sensor;
    }

    public List<Message> getLast(int limit) {
        EntityManager em = Persistence.createEntityManagerFactory("GreyWater").createEntityManager();
        TypedQuery<Message> q = em.createNamedQuery("Message.lastN", Message.class);
        q.setParameter("1", id);
        q.setParameter("2", limit);
        List<Message> list = q.getResultList();
        em.close();
        return list;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Parameters getParameters() {
        return parameters;
    }

    public void setParameters(Parameters parameters) {
        this.parameters = parameters;
    }
}
