package com.greywater.iot.nodeNetwork;

import java.util.List;

/**
 * Created by alexander on 28.11.16.
 */
class LogicalNode extends EvaluableNode<Boolean> {

    LogicalNode() { super(); }

    LogicalNode(List<Node> inputs, String script) {
        super(inputs, script);
    }
}
