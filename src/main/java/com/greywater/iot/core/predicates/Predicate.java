package com.greywater.iot.core.predicates;

import com.greywater.iot.core.Sensor;

public abstract class Predicate {

    Sensor sensor;
    double param;

    Predicate(Sensor sensor, double param) {
        this.sensor = sensor;
        this.param = param;
    }

    public abstract boolean eval();
}
