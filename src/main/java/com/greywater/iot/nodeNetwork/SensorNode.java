package com.greywater.iot.nodeNetwork;

import java.util.List;

/**
 * Created by alexander on 28.11.16.
 */
class SensorNode extends Node<Double> {

    SensorNode() {
        super();
    }

    SensorNode(List<Node> inputs) {
        super(inputs);
    }

}
