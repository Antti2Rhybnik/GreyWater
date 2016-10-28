package com.greywater.iot.jpa;

import com.greywater.iot.vsensors.VirtualSensorAggregator;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/*Таблица виртуальных сенсоров. Еще в разработке*/

@Entity
@Table(name = "VIRTUAL_SENSORS_TABLE")
@NamedQueries({
        @NamedQuery(name = "VirtualSensor.getAll", query = "SELECT s from VirtualSensor s")
})
@XmlRootElement
public class VirtualSensor {

    // === FIELDS === //
    @Id
    @Column(name = "ID")
    private String id = UUID.randomUUID().toString();

    @Column(name = "AGGREGATION_TYPE")
    private String aggregationType;

    @ManyToMany
    @JoinTable(
            name = "VSENSOR__SENSOR",
            joinColumns = @JoinColumn(name = "VSENSOR_ID", referencedColumnName = "ID"),
            inverseJoinColumns = @JoinColumn(name = "SENSOR_ID", referencedColumnName = "ID")
    )
    private List<Sensor> sensors = new ArrayList<>();

    @ManyToMany(mappedBy = "virtualSensors")
    private List<Thing> things = new ArrayList<>();

    // === GETTERS AND SETTERS === //
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAggregationType() {
        return aggregationType;
    }

    public void setAggregationType(String aggregationType) {
        this.aggregationType = aggregationType;
    }

    public List<Sensor> getSensors() {
        return sensors;
    }

    public void setSensors(List<Sensor> sensors) {
        this.sensors = sensors;
    }

    public void addSensor(Sensor sensor) {
        if (!sensors.contains(sensor)) {
            sensors.add(sensor);
        }
    }

    public List<Thing> getThings() {
        return things;
    }

    public void setThings(List<Thing> things) {
        this.things = things;
    }

    // === TRANSIENT === //
    private transient VirtualSensorAggregator vsa;

    public VirtualSensorAggregator getVirtualSensorAggregator() {
        return vsa;
    }

    public void setVirtualSensorAggregator(VirtualSensorAggregator vsa) {
        this.vsa = vsa;
    }

    public void eval() {
        vsa.eval();
        // TODO: and save to DB
    };

    public static List<VirtualSensor> getAll() {
        EntityManager entityManager = Persistence.createEntityManagerFactory("GreyWater").createEntityManager();
        List<VirtualSensor> vsensors = entityManager.createNamedQuery("VirtualSensor.getAll", VirtualSensor.class).getResultList();
        entityManager.close();
        return vsensors;
    }
}
