package com.greywater.iot.nodeNetwork;

import java.util.List;


public class ArithmeticalNode extends EvaluableNode<Double> {

    private boolean integrating = false;


    public ArithmeticalNode() {
        super();
        state = 0.0;
    }

    ArithmeticalNode(List<Node> inputs, String script) {
        super(inputs, script);
    }

    public void eval() {

        Double result = super.evaluateScript();

        if (integrating) {
            state += result;
        } else {
            state = result;
        }
    }


    public boolean isIntegrating() {
        return integrating;
    }

    public void setIntegrating(boolean integrating) {
        this.integrating = integrating;
    }
}

