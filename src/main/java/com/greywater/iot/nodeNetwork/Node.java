package com.greywater.iot.nodeNetwork;

/**
 * Created by alexander on 28.11.16.
 */
import javax.script.*;
import java.util.ArrayList;
import java.util.List;




public class Node<T> {

    Node() {}

    Node(List<Node> inputs) {
        this.inputs = inputs;
    }

    public String getType() { return type; }

    String getId() {
        return id;
    }

    void addInput(Node n) {
        if (inputs == null) inputs = new ArrayList<>();
        if (!inputs.contains(n)) {
            inputs.add(n);
        }
    }

    void setInputs(List<Node> inputs) {
        this.inputs = inputs;
    }

    T getState() {
        return state;
    }

    private String id;
    List<Node> inputs;
    T state;
    String type;

}
