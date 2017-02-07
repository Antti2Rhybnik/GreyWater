package com.greywater.iot.nodeNetwork;

import javax.script.*;
import java.util.List;


public class EvaluableNode<T> extends Node<T> {

    private static Compilable engine = (Compilable) new ScriptEngineManager().getEngineByName("javascript");
    private String expr;


    private CompiledScript cs;

    EvaluableNode() {
        super();
    }

    public String getExpr() {
        return expr;
    }


    public void setExpr(String script) {
        try {
            expr = script;
            cs = engine.compile(script);

        } catch(ScriptException e)  {
            e.printStackTrace();
        }
    }

    T evaluateScript() {

        Bindings bindings = new SimpleBindings();
        T res = null;

        inputs.forEach(node -> {
            bindings.put(node.getId(), node.getState());
        });

        try {

            res = (T) cs.eval(bindings);

        } catch (ScriptException | ClassCastException e) {
            e.printStackTrace();
        }

        return res;
    }

    void eval() {

    }


}

