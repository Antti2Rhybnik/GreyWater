package com.greywater.iot.nodeNetwork;


import javax.script.Compilable;
import javax.script.ScriptEngineManager;
import java.util.ArrayList;
import java.util.List;

import com.greywater.iot.jpa.Message;


public class NodeMaster {

    public static Compilable engine = (Compilable) new ScriptEngineManager().getEngineByName("javascript");

    static List<Node> allNodes = new ArrayList<>();
    static List<SensorNode> sensorNodes = new ArrayList<>();
    static List<ArithmeticalNode> arithmeticalNodes = new ArrayList<>();
    static List<LogicalNode> logicalNodes = new ArrayList<>();
    static List<EventNode> eventNodes = new ArrayList<>();


    public static void process() {

        Message.updateLastMessages();
        sensorNodes.forEach(sn -> sn.eval());
        arithmeticalNodes.forEach(an -> an.eval());
        logicalNodes.forEach(ln -> ln.eval());
        eventNodes.forEach(en -> en.eval());

    }


}
