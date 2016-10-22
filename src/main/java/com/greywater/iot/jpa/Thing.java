package com.greywater.iot.jpa;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "THINGS_TABLE", catalog = "")
@XmlRootElement
public class Thing {

    @Id
    @Column(name = "ID")
    private long id;

    @Column(name = "NAME")
    private String name;

    @OneToMany(mappedBy = "thing", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Sensor> sensors = new ArrayList<>();

    public void addSensor(Sensor sensorEntity) {

        if (sensorEntity.getThing() != this) {
            this.sensors.add(sensorEntity);
            sensorEntity.setThing(this);

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

    public List<Sensor> getSensorEntities() {
        return sensors;
    }

    public void setSensorEntities(List<Sensor> sensors) {
        this.sensors = sensors;
    }
}
