package com.greywater.iot.jpa;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by alexander on 10/12/16.
 */
@Entity
@Table(name="LIGHT_SENSOR", schema = "NEO_77I8IO0F4PQ8TZ67A28RD0L2L")
@NamedQueries({
        @NamedQuery(name="getAllLightSensors",query = "SELECT ws from WaterSensor ws")
})
@XmlRootElement
public class LightSensor extends VirtualSensor{

    @Column(name="SENSOR_VALUE")
    private Double sensorValue = null;


    @Override
    public void addSensorValue(SensorsTableEntity sensorsTableEntity) {
        this.setgCreated(sensorsTableEntity.getgCreated());
        this.setSensorValue(sensorsTableEntity.getSensorValue());
        this.setgDevice(sensorsTableEntity.getgDevice());
        computeVirtualSensorValue();

    }

    @Override
    public void computeVirtualSensorValue() {
        EntityManager entityManager = Persistence.createEntityManagerFactory("GreyWater").createEntityManager();
        entityManager.getTransaction().begin();
        entityManager.persist(this);
        entityManager.getTransaction().commit();
        entityManager.close();
    }

    public Double getSensorValue() {
        return sensorValue;
    }

    public void setSensorValue(Double sensorValue) {
        this.sensorValue = sensorValue;
    }


    public double getAverageValue(){
        EntityManager entityManager = Persistence.createEntityManagerFactory("GreyWater").createEntityManager();
        Query query = entityManager.createNativeQuery("select avg(ws.sensorValuee) FROM WaterSensor ws");
        Double average = (Double) query.getSingleResult();
        entityManager.close();
        return average;
    }
}
