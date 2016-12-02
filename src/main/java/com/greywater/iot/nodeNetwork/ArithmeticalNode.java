package com.greywater.iot.nodeNetwork;

import java.util.List;


public class ArithmeticalNode extends EvaluableNode<Double> {

    public ArithmeticalNode() {
        super();
    }

    ArithmeticalNode(List<Node> inputs, String script) {
        super(inputs, script);
    }
}

