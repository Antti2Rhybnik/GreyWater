package com.greywater.iot.jpa;

import com.greywater.iot.vsensors.Multiplicator;
import com.greywater.iot.vsensors.SensorNullMessageException;
import com.greywater.iot.vsensors.SimpleRedirector;
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

    public VirtualSensor() {}

    @PostLoad
    void postLoad() {
        switch (this.getAggregationType()) {
            case "SIMPLE_REDIRECTOR":
                this.setVirtualSensorAggregator(new SimpleRedirector(this));
                break;
            case "MULTIPLICATOR":
                this.setVirtualSensorAggregator(new Multiplicator(this));
                break;
            default:
                System.err.println("Unsupported aggregation type");
        }
    }

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
        try {
            Double val = vsa.eval();
            // TODO: and save to DB
            //EntityManager entityManager = Persistence.createEntityManagerFactory("GreyWater").createEntityManager();
            System.out.println("evaluated: " + val);

        } catch (SensorNullMessageException ex) {
            System.err.println(ex.getMessage());
        }
    };

    public static List<VirtualSensor> getAll() {
        EntityManager entityManager = Persistence.createEntityManagerFactory("GreyWater").createEntityManager();
        List<VirtualSensor> vsensors = entityManager.createNamedQuery("VirtualSensor.getAll", VirtualSensor.class).getResultList();
        entityManager.close();
        return vsensors;
    }

    // === FIELDS === //
    @Id
    @Column(name = "VS_ID")
    private String id = UUID.randomUUID().toString();

    @Column(name = "AGGREGATION_TYPE")
    private String aggregationType;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "VSENSOR__SENSOR",
            joinColumns = @JoinColumn(name = "VSENSOR_ID", referencedColumnName = "ID", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "SENSOR_ID", referencedColumnName = "ID", nullable = false)
    )
    private List<Sensor> sensors = new ArrayList<>();

    @ManyToMany(mappedBy = "virtualSensors")
    private List<Thing> things = new ArrayList<>();

    @OneToMany(mappedBy = "virtualSensor")
    private List<VirtualMessage> virtualMessages;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PARAMETERS_ID")
    private Parameters parameters;


}
