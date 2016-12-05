package com.greywater.iot.nodeNetwork;


import javax.script.Compilable;
import javax.script.ScriptEngineManager;
import java.util.ArrayList;
import java.util.List;

import com.greywater.iot.jpa.Message;


public class NodeMaster {



    public static List<Node> allNodes = new ArrayList<>();
    public static List<SensorNode> sensorNodes = new ArrayList<>();
    public static List<ArithmeticalNode> arithmeticalNodes = new ArrayList<>();
    public static List<LogicalNode> logicalNodes = new ArrayList<>();
    public static List<EventNode> eventNodes = new ArrayList<>();


    public static void process() {

        Message.updateLastMessages();
        sensorNodes.forEach(SensorNode::eval);
        arithmeticalNodes.forEach(ArithmeticalNode::eval);
        logicalNodes.forEach(LogicalNode::eval);
        eventNodes.forEach(EventNode::eval);

    }


}
