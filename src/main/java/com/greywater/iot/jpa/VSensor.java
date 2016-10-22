package com.greywater.iot.jpa;


import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="SENSORS_TABLE")
@NamedQueries({
        @NamedQuery(name = "Sensor.getAll", query = "SELECT s from Sensor s"),
        @NamedQuery(name = "Sensor.getByID", query = "SELECT s from Sensor s where s.id = :id")
})
@XmlRootElement
public class VSensor implements Serializable {

    @Id
    @Column(name = "ID")
    @GeneratedValue
    private Integer id;

    @Column(name = "TYPE")
    private String type;

    @OneToMany(mappedBy = "virtualSensors", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Predicate> predicates = new ArrayList<>();


    @ManyToMany(mappedBy = "virtualSensors", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Sensor> sensors = new ArrayList<>();



}
