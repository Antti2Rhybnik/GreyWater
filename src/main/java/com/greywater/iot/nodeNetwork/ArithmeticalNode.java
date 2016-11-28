package com.greywater.iot.nodeNetwork;

import java.util.List;

/**
 * Created by alexander on 28.11.16.
 */
class ArithmeticalNode extends EvaluableNode<Double> {

    ArithmeticalNode() {
        super();
    }

    ArithmeticalNode(List<Node> inputs, String script) {
        super(inputs, script);
    }
}

