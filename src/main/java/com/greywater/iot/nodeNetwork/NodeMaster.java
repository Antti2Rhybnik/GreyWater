package com.greywater.iot.nodeNetwork;


import javax.script.Compilable;
import javax.script.ScriptEngineManager;
import javax.servlet.annotation.WebListener;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.greywater.iot.config.ConfigManager;
import com.greywater.iot.jpa.Message;


public class NodeMaster {

    private static ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    public static List<Node> allNodes = new ArrayList<>();
    public static List<SensorNode> sensorNodes = new ArrayList<>();
    public static List<ArithmeticalNode> arithmeticalNodes = new ArrayList<>();
    private static List<LogicalNode> logicalNodes = new ArrayList<>();
    private static List<EventNode> eventNodes = new ArrayList<>();


    public static void process() {

        Message.updateLastMessages();
        sensorNodes.forEach(SensorNode::eval);
        arithmeticalNodes.forEach(ArithmeticalNode::eval);
        logicalNodes.forEach(LogicalNode::eval);
        eventNodes.forEach(EventNode::eval);

    }

    public static void init1() {
        //constructObjects();
        //Waterflow
        SensorNode sn = new SensorNode();
        sn.setSensorId(1L);
        sn.setId("_1");
        sn.setSensorType("WATERFLOW");
        sensorNodes.add(sn);
        allNodes.add(sn);

        ArithmeticalNode an = new ArithmeticalNode();
        an.setId("_2");
        an.addInput(sn);
        an.setExpr("_1");
        an.setIntegrable(true);
        arithmeticalNodes.add(an);
        allNodes.add(an);

        LogicalNode ln = new LogicalNode();
        ln.setId("_3");
        ln.addInput(an);
        ln.setExpr("_2 > 300");
        logicalNodes.add(ln);
        allNodes.add(ln);

        EventNode en = new EventNode();
        en.setId("_4");

        en.setMessage("vse ploho");
        en.setImportance("1");
        en.addInput(ln);
        eventNodes.add(en);
        allNodes.add(en);

        //LightSensor
        SensorNode lightSensor = new SensorNode();
        lightSensor.setSensorId(2L);
        lightSensor.setId("_5");
        lightSensor.setSensorType("Light");
        sensorNodes.add(lightSensor);
        allNodes.add(lightSensor);


        ArithmeticalNode arithmeticalLightNode = new ArithmeticalNode();
        arithmeticalLightNode.setId("_6");
        arithmeticalLightNode.addInput(lightSensor);
        arithmeticalLightNode.setExpr("_5");
        arithmeticalNodes.add(arithmeticalLightNode);
        allNodes.add(arithmeticalLightNode);


        //LightSensor
        SensorNode tempSensor = new SensorNode();
        tempSensor.setSensorId(3L);
        tempSensor.setId("_7");
        tempSensor.setSensorType("Temp");
        sensorNodes.add(tempSensor);
        allNodes.add(tempSensor);

        ArithmeticalNode arithmeticalTempNode = new ArithmeticalNode();
        arithmeticalTempNode.setId("_8");
        arithmeticalTempNode.addInput(tempSensor);
        arithmeticalTempNode.setExpr("_7");
        arithmeticalNodes.add(arithmeticalTempNode);
        allNodes.add(arithmeticalTempNode);

    }

    public static void init() {
        init1();
        scheduler.scheduleAtFixedRate(NodeMaster::process,0, 3, TimeUnit.SECONDS);
    }

    private static void constructObjects() {

        sensorNodes = ConfigManager.getSensorNodes();
        arithmeticalNodes = ConfigManager.getArithmeticalNodes();
        logicalNodes = ConfigManager.getLogicalNodes();
        eventNodes = ConfigManager.getEventNodes();


        allNodes = new ArrayList<>();

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


    public static void stop() {
        scheduler.shutdown();
    }
}
