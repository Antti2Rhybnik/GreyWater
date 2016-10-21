package com.greywater.iot.jpa;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by alexander on 10/13/16.
 */
/*Самый интересный класс. Это штука. Ну как сказать...
  Сложно подобрать слово лучше. Мы пытались, честно.
* Этот класс абстрагируют реальные физические штуки, такие как емкость с водой.
* Возможно нагреватель или еще какую то вещь за которой нам следует наблюдать.*/
@Entity
@Table(name = "THINGS_TABLE", catalog = "")
@XmlRootElement
public class Thing {

    @Id
    @Column(name = "ID")
    @GeneratedValue
    private long id;


    @Column(name = "NAME")
    private String name;

    @OneToMany(mappedBy = "thing", cascade = CascadeType.ALL)
    private List<Sensor> sensors = new ArrayList<Sensor>();

    public void addSensor(Sensor sensor){

        if(sensor.getThing()!=this){
            this.sensors.add(sensor);
            sensor.setThing(this);

        }
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Sensor> getSensors() {
        return sensors;
    }

    public void setSensors(List<Sensor> sensors) {
        this.sensors = sensors;
    }
}
