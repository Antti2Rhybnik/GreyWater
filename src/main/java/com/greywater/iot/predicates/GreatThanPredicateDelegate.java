package com.greywater.iot.predicates;

import com.greywater.iot.jpa.Predicate;
import com.greywater.iot.jpa.VSensor;


public class GreatThanPredicateDelegate extends PredicateDelegate {


    public GreatThanPredicateDelegate(VSensor sensor, Predicate pr) {
        super(sensor, pr);
    }

    @Override
    public boolean eval() {
        // TODO: на самом деле getLastValue из таблицы вируальных сообщений
        // или не LastValue - в общем, getТоЧтоНужноИЧтоЕщёНеОбработанно
        double value = vsensor.eval();
        return value > predicate.getValue();
    }

    @Override
    public String toString() {
        return "GT Predicate";
    }
}
