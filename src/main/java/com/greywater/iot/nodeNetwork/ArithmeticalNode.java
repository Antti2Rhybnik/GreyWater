package com.greywater.iot.nodeNetwork;

import java.util.List;


public class ArithmeticalNode extends EvaluableNode<Double> {

    private boolean integrable = false;


    public ArithmeticalNode() {
        super();
        state = 0.0;
    }

    // eval method should update state
    public void eval() {

        Double result = super.evaluateScript();

        if (integrable) {
            state += result;
        } else {
            state = result;
        }

        System.out.println("ArithmeticalNode: " + state);

    }


    public boolean isIntegrable() {
        return integrable;
    }

    public void setIntegrable(boolean integrable) {
        this.integrable = integrable;
    }
}

