package com.greywater.iot.jpa;


import com.greywater.iot.vsensors.VSensorDelegate;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="VSENSORS_TABLE")
@NamedQueries({
        @NamedQuery(name = "Sensor.getAll", query = "SELECT s from Sensor s"),
        @NamedQuery(name = "Sensor.getByID", query = "SELECT s from Sensor s where s.id = :id")
})
@XmlRootElement
public class VSensor implements Serializable {

    @Id
    @Column(name = "VSENSOR_ID")
    @GeneratedValue
    private Integer id;

    @Column(name = "TYPE")
    private String type;

    // список "предикатов", которые обрабатываются для этого виртуального сенсора
    @OneToMany(mappedBy = "vsensor", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Predicate> predicates = new ArrayList<>();

    // пока непонятно, как это всё нормально соединить
    // список сырых сенсоров, которые участвуют в формировании значения на виртуальном сенсоре
    @ManyToMany(mappedBy = "vsensors", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(
            name = "VSENS_SENS",
            joinColumns = @JoinColumn(name = "VSENS_ID", referencedColumnName = "VSENSOR_ID"),
            inverseJoinColumns = @JoinColumn(name = "SENS_ID", referencedColumnName = "SENSOR_ID")
    )
    private List<Sensor> sensors = new ArrayList<>();

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

    public List<Predicate> getPredicates() {
        return predicates;
    }

    public void setPredicates(List<Predicate> predicates) {
        this.predicates = predicates;
    }

    public List<Sensor> getSensors() {
        return sensors;
    }

    public void setSensors(List<Sensor> sensors) {
        this.sensors = sensors;
    }

    // transient - не участвует в persistence операциях
    // делегат для вычисления значения виртуального сенсора
    transient VSensorDelegate vSensorDelegate;

    public void setvSensorDelegate(VSensorDelegate vsd) {
        this.vSensorDelegate = vsd;
    }

    // сам делегирующий метод
    public Double eval() {
        return vSensorDelegate.eval();
    }


    public static List<VSensor> getAll() {
        return new ArrayList<VSensor>();
    }
}
