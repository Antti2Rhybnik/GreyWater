package com.greywater.iot.jpa;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by alexander on 10/12/16.
 */

@Entity
@Table(name="WATER_LIGHT_COMBO_SENSOR", schema = "NEO_77I8IO0F4PQ8TZ67A28RD0L2L")
@NamedQueries({
        @NamedQuery(name="getAllWaterLight",query = "SELECT ws from WaterWaterLightComboSensorSensor ws")
})
@XmlRootElement
public class WaterLightComboSensor extends VirtualSensor {

    @Column(name="SENSOR_VALUE")
    private Double sensorValue = null;
    @Column(name="WATER_SENSOR_VALUE")
    private Double waterSensor=null;
    @Column(name="LIGHT_SENSOR_VALUE")
    private Double lightSensor=null;


    @Override
    public void addSensorValue(MessageTableEntity messageTableEntity) {
        switch (messageTableEntity.getSensorClass()){
            case "WATER":{
                this.setWaterSensor(messageTableEntity.getSensorValue());
            }
            break;
            case "LIGHT":{
                this.setLightSensor(messageTableEntity.getSensorValue());
            }
        }
        this.setgCreated(messageTableEntity.getgCreated());
        this.setgDevice(messageTableEntity.getgDevice());
        computeVirtualSensorValue();
    }

    @Override
    public void computeVirtualSensorValue() {
        EntityManager entityManager = Persistence.createEntityManagerFactory("GreyWater").createEntityManager();
        if(waterSensor==null ||lightSensor==null ){
            System.out.println("nulls");
        }



    }


    public Double getWaterSensor() {
        return waterSensor;
    }

    public void setWaterSensor(Double waterSensor) {
        this.waterSensor = waterSensor;
    }

    public Double getLightSensor() {
        return lightSensor;
    }

    public void setLightSensor(Double lightSensor) {
        this.lightSensor = lightSensor;
    }
}
