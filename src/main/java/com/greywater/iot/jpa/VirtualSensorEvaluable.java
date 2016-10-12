package com.greywater.iot.jpa;

/**
 * Created by alexander on 10/12/16.
 */
public interface VirtualSensorEvaluable {
    //Add sensor value to virtual sensor and then evaluate virtual sensor logic
     void addSensorValue(MessageTableEntity messageTableEntity);

    //evaluation method for virtual sensor
    void computeVirtualSensorValue();
}
