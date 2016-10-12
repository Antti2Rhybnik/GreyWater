package com.greywater.iot.jpa;

import javax.persistence.*;

/**
 * Created by alexander on 10/12/16.
 */
@Entity
@Table(name="WATER_SENSOR")
@NamedQueries({
        @NamedQuery(name="getAll",query = "SELECT ws from WaterSensor ws")
})
public class WaterSensor extends VirtualSensor {

    @Column(name="SENSOR_VALUE")
    private Double sensorValue = null;


    @Override
    public void addSensorValue(SensorsTableEntity sensorsTableEntity) {
        this.setgCreated(sensorsTableEntity.getgCreated());
        this.setSensorValue(sensorsTableEntity.getSensorValue());
        this.setgDevice(sensorsTableEntity.getgDevice());

    }

    @Override
    public void computeVirtualSensorValue() {

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
