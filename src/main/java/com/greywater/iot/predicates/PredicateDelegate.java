package com.greywater.iot.predicates;

import sun.management.Sensor;

public abstract class PredicateDelegate {

    Sensor sensor;
    double param;

    PredicateDelegate(Sensor sensor, double param) {
        this.sensor = sensor;
        this.param = param;
    }

    public abstract boolean eval();
}
