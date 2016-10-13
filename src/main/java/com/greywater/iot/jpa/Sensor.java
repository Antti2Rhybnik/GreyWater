package com.greywater.iot.jpa;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by alexander on 10/13/16.
 */
@Entity
@Table(name="SENSORS_TABLE")
@NamedQuery(name = "Sensor.getAll", query = "SELECT s from Sensor s")
public class Sensor {

    @Id
    @Column(name = "ID")
    @GeneratedValue
    private long id;

    @Column(name = "TYPE")
    private String type = null;

    @ManyToOne( cascade = CascadeType.ALL)
    @JoinColumn(name = "THING_ID")
    private Thing thing;



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

    public static List<Sensor> getAllSensors(){
        EntityManager entityManager = Persistence.createEntityManagerFactory("GreyWater").createEntityManager();
        List<Sensor> sensors = entityManager.createNamedQuery("Sensor.getAll", Sensor.class).getResultList();
        entityManager.close();
        return sensors;
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


}
