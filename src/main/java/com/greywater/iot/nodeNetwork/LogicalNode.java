package com.greywater.iot.nodeNetwork;

import java.util.List;

public class LogicalNode extends EvaluableNode<Boolean> {

    public LogicalNode() {
        super();
        type = "logical";
    }

    public void eval() {
        state = super.evaluateScript();

        System.out.println("LogicalNode " + id + ": " + state);
    }
}
