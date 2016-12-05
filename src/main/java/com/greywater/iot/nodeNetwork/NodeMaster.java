package com.greywater.iot.nodeNetwork;


import javax.script.Compilable;
import javax.script.ScriptEngineManager;
import javax.servlet.annotation.WebListener;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.greywater.iot.config.ConfigManager;
import com.greywater.iot.jpa.Message;


public class NodeMaster {

    private static ScheduledExecutorService scheduler;
    private static List<Node> allNodes = new ArrayList<>();
    private static List<SensorNode> sensorNodes = new ArrayList<>();
    private static List<ArithmeticalNode> arithmeticalNodes = new ArrayList<>();
    private static List<LogicalNode> logicalNodes = new ArrayList<>();
    private static List<EventNode> eventNodes = new ArrayList<>();


    public static void process() {

        Message.updateLastMessages();
        sensorNodes.forEach(SensorNode::eval);
        arithmeticalNodes.forEach(ArithmeticalNode::eval);
        logicalNodes.forEach(LogicalNode::eval);
        eventNodes.forEach(EventNode::eval);

    }


    public static void init() {
        constructObjects();

        scheduler.schedule(NodeMaster::process, 10, TimeUnit.SECONDS);
    }

    private static void constructObjects() {

        sensorNodes = ConfigManager.getSensorNodes();
        arithmeticalNodes = ConfigManager.getArithmeticalNodes();
        logicalNodes = ConfigManager.getLogicalNodes();
        eventNodes = ConfigManager.getEventNodes();


        List<Node> allNodes = new ArrayList<>();

        allNodes.addAll(sensorNodes);
        allNodes.addAll(arithmeticalNodes);
        allNodes.addAll(logicalNodes);
        allNodes.addAll(eventNodes);


        for (Node node: allNodes) {

            List<String> parentsIDs = ConfigManager.getParentsForNode(node.getId());

            for (String parentId : parentsIDs) {

                Node nn = allNodes
                        .stream()
                        .filter((n -> parentId.equals(n.getId())))
                        .findFirst()
                        .get();

                node.addInput(nn);
            }
        }

    }



}
