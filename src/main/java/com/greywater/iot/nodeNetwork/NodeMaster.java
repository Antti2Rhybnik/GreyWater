package com.greywater.iot.nodeNetwork;


import com.greywater.iot.jpa.Event;

import javax.naming.NamingException;
import javax.script.Compilable;
import javax.script.ScriptEngineManager;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.greywater.iot.persistence.PersistManager;

public class NodeMaster {

    public static Compilable engine = (Compilable) new ScriptEngineManager().getEngineByName("javascript");

    static List<Node> allNodes = new ArrayList<>();
    static List<SensorNode> sensorNodes = new ArrayList<>();
    static List<ArithmeticalNode> arithmeticalNodes = new ArrayList<>();
    static List<LogicalNode> logicalNodes = new ArrayList<>();
    static List<EventNode> eventNodes = new ArrayList<>();


    public static void saveNodesConfig(String config) throws IOException, SQLException, NamingException {
        // TODO: parse and write
        // parse
        ObjectMapper mapper = new ObjectMapper();

        List<JsonNode> nodes = Arrays.asList(mapper.readValue(config, JsonNode[].class));


        for (JsonNode jsonNode: nodes) {
            String nodeID = jsonNode.get("node_id").asText();
            String nodeType = jsonNode.get("node_type").asText();
            // filling NODES table
            writeNode(nodeID, nodeType);

            // filling NODE__NODE table
            List<String> parentIDs = Arrays.asList(mapper.readValue(jsonNode.get("parents").textValue(), String[].class));
            for (String parentId: parentIDs) {
                writeRelation(nodeID, parentId);
            }

            // filling other
            JsonNode params = mapper.readValue(jsonNode.get("parameters").textValue(), JsonNode.class);


            switch (nodeType) {
                case "sensor":
                    String sensorType = params.get("sensor_type").asText();
                    //writeNode();
            }




        }

    }


    public static void writeNode(String id, String type) throws SQLException, NamingException {

        String sqlQuery = "insert into NODES(NODE_ID,NODE_TYPE) values(?,?)";

        Connection conn = PersistManager.newConnection();
        PreparedStatement pstmt = conn.prepareStatement(sqlQuery);

        pstmt.setString(1, id);
        pstmt.setString(2, type);

        pstmt.executeQuery();

    }

    public static void writeRelation(String childId, String parentId) throws SQLException, NamingException {

        String sqlQuery = "insert into NODE__NODE(CHILD_ID,PARENT_ID) values(?,?)";

        Connection conn = PersistManager.newConnection();
        PreparedStatement pstmt = conn.prepareStatement(sqlQuery);

        pstmt.setString(1, childId);
        pstmt.setString(2, parentId);

        pstmt.executeQuery();

    }


    public static void writeSensorNode(String type) throws SQLException, NamingException {

        String sqlQuery = "insert into SENSOR_NODES(SN_ID,SENSOR_TYPE) values(?,?)";

        Connection conn = PersistManager.newConnection();
        PreparedStatement pstmt = conn.prepareStatement(sqlQuery);

        String id = UUID.randomUUID().toString();

        pstmt.setString(1, id);
        pstmt.setString(2, type);

        pstmt.executeQuery();

    }


}
