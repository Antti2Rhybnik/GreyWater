package com.greywater.iot.nodeNetwork;

import java.util.List;

public class LogicalNode extends EvaluableNode<Boolean> {

    LogicalNode() { super(); }

    LogicalNode(List<Node> inputs, String script) {
        super(inputs, script);
    }
}
