package com.greywater.iot.nodeNetwork;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.greywater.iot.config.ConfigManager;
import com.greywater.iot.jpa.Message;
import com.greywater.iot.rest.RandomServerException;


public class NodeMaster {

    private static ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    public static List<Node> allNodes = new ArrayList<>();
    public static List<SensorNode> sensorNodes = new ArrayList<>();
    public static List<ArithmeticalNode> arithmeticalNodes = new ArrayList<>();
    private static List<LogicalNode> logicalNodes = new ArrayList<>();
    private static List<EventNode> eventNodes = new ArrayList<>();
    public static boolean constructed = false;


    public static void process() {

        Message.updateLastMessages();
        sensorNodes.forEach(SensorNode::eval);
        arithmeticalNodes.forEach(ArithmeticalNode::eval);
        logicalNodes.forEach(LogicalNode::eval);
        eventNodes.forEach(EventNode::eval);

    }

    public static void start() throws RandomServerException {
        if (!constructed) {
            try {
                constructObjects();
                constructed = true;
            } catch (IOException e) {
                throw new RandomServerException("Random is force", e);
            }
        }
        scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(NodeMaster::process, 0, 2, TimeUnit.SECONDS);
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

        scheduler.scheduleAtFixedRate(NodeMaster::process, 0, 3, TimeUnit.SECONDS);
    }

    public static void constructObjects() throws IOException {

        allNodes = new ArrayList<>();
        sensorNodes = new ArrayList<>();
        arithmeticalNodes = new ArrayList<>();
        logicalNodes = new ArrayList<>();
        eventNodes = new ArrayList<>();

        try {
            String config = ConfigManager.loadConfig();

//            System.out.println(config);
            // parse
            ObjectMapper mapper = new ObjectMapper();

            List<JsonNode> nodes = Arrays.asList(mapper.readValue(config, JsonNode[].class));

            for (JsonNode jsonNode : nodes) {


                String nodeID = jsonNode.get("node_id").asText();
                String nodeType = jsonNode.get("node_type").asText();

//                System.out.println(nodeID + " " + nodeType);

                JsonNode params = mapper.readValue(String.valueOf(jsonNode.get("parameters")), JsonNode.class);

                switch (nodeType) {

                    case "sensor":

                        Long sensorId = params.get("sensor_id").asLong();
                        String sensorType = params.get("sensor_type").asText();
                        String sensorUnit = params.get("sensor_unit").asText();

//                        System.out.println(sensorId + " "  + sensorType + " " + sensorUnit);

                        SensorNode sn = new SensorNode();
                        sn.setSensorId(sensorId);
                        sn.setSensorType(sensorType);
                        sn.setSensorUnit(sensorUnit);
                        sn.setId(nodeID);
                        sensorNodes.add(sn);
                        allNodes.add(sn);
                        break;
                    case "arithmetical":

                        String arithmExpr = params.get("expr").asText();
                        Boolean arithmIntegrable = params.get("integrable").asBoolean();

                        ArithmeticalNode an = new ArithmeticalNode();
                        an.setExpr(arithmExpr);
                        an.setIntegrable(arithmIntegrable);
                        an.setId(nodeID);
                        arithmeticalNodes.add(an);
                        allNodes.add(an);
                        break;
                    case "logical":

                        String logicExpr = params.get("expr").asText();

                        LogicalNode ln = new LogicalNode();
                        ln.setExpr(logicExpr);
                        ln.setId(nodeID);
                        logicalNodes.add(ln);
                        allNodes.add(ln);
                        break;
                    case "event":

                        String importance = params.get("importance").asText();
                        String msg = params.get("msg").asText();

                        EventNode en = new EventNode();
                        en.setMessage(msg);
                        en.setImportance(importance);
                        en.setId(nodeID);
                        eventNodes.add(en);
                        allNodes.add(en);
                        break;

                    default:
                        break;
                }

            } // end for


            for(Node node: allNodes) {
                System.out.println(node.getId());
            }

            for (JsonNode jsonNode : nodes) {

                String nodeID = jsonNode.get("node_id").asText();

                System.out.println(nodeID);

                Node node = allNodes
                        .stream()
                        .filter((n -> nodeID.equals(n.getId())))
                        .findFirst()
                        .get();

                List<String> parentIDs = Arrays.asList(mapper.readValue(String.valueOf(jsonNode.get("parents")), String[].class));


                for (String parentId : parentIDs) {

                    Node nn = allNodes
                            .stream()
                            .filter((n -> parentId.equals(n.getId())))
                            .findFirst()
                            .get();

                    node.addInput(nn);
                }

            }

        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }




    public static void stop() {
        scheduler.shutdown();
        constructed = false;
    }
}
