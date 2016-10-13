package com.greywater.iot.jpa;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;
import java.util.UUID;

/**
 * Created by alexander on 10/13/16.
 */
@Entity
@Table(name = "THINGS_TABLE", schema = "NEO_77I8IO0F4PQ8TZ67A28RD0L2L", catalog = "")
@XmlRootElement
public class Thing {

    @Id
    @Column(name = "ID")
    @GeneratedValue
    private long id;


    @Column(name = "NAME")
    private String name;

    @OneToMany(mappedBy = "thing", cascade = CascadeType.ALL)
    private List<Sensor> sensors;

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
