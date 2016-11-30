package com.greywater.iot.nodeNetwork;

import java.util.List;


public class ArithmeticalNode extends EvaluableNode<Double> {

    ArithmeticalNode() {
        super();
    }

    ArithmeticalNode(List<Node> inputs, String script) {
        super(inputs, script);
    }
}

