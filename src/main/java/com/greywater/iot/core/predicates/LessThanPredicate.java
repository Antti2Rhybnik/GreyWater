package com.greywater.iot.core.predicates;

import com.greywater.iot.core.Sensor;

/**
 * Created by antti on 16.10.16.
 */
public class LessThanPredicate extends Predicate {


    public LessThanPredicate(Sensor sensor, double param) {
        super(sensor, param);
    }

    @Override
    public boolean eval() {
        double actualValue = sensor.getActualValue();
        return actualValue < param;
    }

    @Override
    public String toString() {
        return "LT Predicate";
    }
}
