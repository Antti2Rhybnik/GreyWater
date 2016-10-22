package com.greywater.iot.predicates;

import sun.management.Sensor;

/**
 * Created by antti on 16.10.16.
 */
public class GreatThanPredicateDelegate extends PredicateDelegate {


    public GreatThanPredicateDelegate(Sensor sensor, double param) {
        super(sensor, param);
    }

    @Override
    public boolean eval() {
        double actualValue = sensor.getActualValue();
        return actualValue > param;
    }

    @Override
    public String toString() {
        return "GT Predicate";
    }
}
