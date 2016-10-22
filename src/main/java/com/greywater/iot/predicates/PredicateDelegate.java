package com.greywater.iot.predicates;

import com.greywater.iot.jpa.Predicate;
import com.greywater.iot.jpa.VSensor;

public abstract class PredicateDelegate {

    VSensor vsensor;
    Predicate predicate;

    PredicateDelegate(VSensor vsensor, Predicate predicate) {
        this.vsensor = vsensor;
        this.predicate = predicate;
    }

    public abstract boolean eval();
}
