package com.greywater.iot.jpa;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by alexander on 10/12/16.
 */
@Entity
@Table(name="WATER_SENSOR")
public class WaterSensor extends VirtualSensor {



    @Override
    public void addSensorValue(SensorsTableEntity sensorsTableEntity) {

    }

    @Override
    public void computeVirtualSensorValue() {

    }
}
