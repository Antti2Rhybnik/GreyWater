package com.greywater.iot.nodeNetwork;

import javax.script.*;
import java.util.List;


public class EvaluableNode<T> extends Node<T> {

    private CompiledScript cs;

    EvaluableNode() {
        super();
    }

    EvaluableNode(List<Node> inputs, String script) {
        super(inputs);

        try {

            cs = NodeMaster.engine.compile(script);

        } catch(ScriptException scrEx)  {
            scrEx.printStackTrace();
        }
    }


    void eval() {

        Bindings bindings = new SimpleBindings();

        inputs.forEach(node -> {
            bindings.put(node.getId(), node.getState());
        });

        try {
            state = (T) cs.eval(bindings);
        } catch (ScriptException | ClassCastException e) {
            e.printStackTrace();
        }
    }


}

