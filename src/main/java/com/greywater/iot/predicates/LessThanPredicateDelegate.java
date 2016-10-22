package com.greywater.iot.predicates;

import com.greywater.iot.jpa.Predicate;
import com.greywater.iot.jpa.VSensor;


public class LessThanPredicateDelegate extends PredicateDelegate {


    public LessThanPredicateDelegate(VSensor sensor, Predicate pr) {
        super(sensor, pr);
    }

    @Override
    public boolean eval() {
        double value = vsensor.eval();
        return value < predicate.getValue();
    }

    @Override
    public String toString() {
        return "LT Predicate";
    }
}
