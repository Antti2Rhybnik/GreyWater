package com.greywater.iot.config;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.greywater.iot.nodeNetwork.*;
import com.greywater.iot.persistence.PersistManager;

import javax.naming.NamingException;
import java.io.IOException;
import java.sql.*;
import java.util.Arrays;
import java.util.List;

import static com.greywater.iot.nodeNetwork.NodeMaster.allNodes;
import static com.greywater.iot.nodeNetwork.NodeMaster.sensorNodes;
import static com.greywater.iot.nodeNetwork.NodeMaster.arithmeticalNodes;
import static com.greywater.iot.nodeNetwork.NodeMaster.logicalNodes;
import static com.greywater.iot.nodeNetwork.NodeMaster.eventNodes;


public class ConfigManager {


    public static void saveConfig(String config) throws IOException, SQLException, NamingException {

        deleteConfig();
        // parse
        ObjectMapper mapper = new ObjectMapper();

        List<JsonNode> nodes = Arrays.asList(mapper.readValue(config, JsonNode[].class));

        Connection conn = PersistManager.newConnection();

        for (JsonNode jsonNode: nodes) {

            String nodeID = jsonNode.get("node_id").asText();
            String nodeType = jsonNode.get("node_type").asText();
            // filling NODES table
            writeNode(nodeID, nodeType, conn);

            // filling NODE__NODE table
            List<String> parentIDs = Arrays.asList(mapper.readValue(jsonNode.get("parents").textValue(), String[].class));
            for (String parentId: parentIDs) {
                writeRelation(nodeID, parentId, conn);
            }

            // filling other
            JsonNode params = mapper.readValue(jsonNode.get("parameters").textValue(), JsonNode.class);


            switch (nodeType) {

                case "sensor":
                    String sensorType = params.get("sensor_type").asText();
                    writeSensorNode(nodeID, sensorType, conn);
                    break;
                case "arithmetical":
                    String arithmExpr = params.get("arithm_expr").asText();
                    writeArithmeticalNode(nodeID, arithmExpr, conn);
                    break;
                case "logical":
                    String logicExpr = params.get("logic_expr").asText();
                    writeLogicalNode(nodeID, logicExpr, conn);
                    break;
                case "event":
                    String importance = params.get("event_importance").asText();
                    String msg = params.get("event_msg").asText();
                    writeEventNode(nodeID, importance, msg, conn);
                    break;

                default: break;
            }

        } // end for

        conn.close();

    }

    // SOME HELPFUL METHODS!!1
    public static void writeNode(String id, String type, Connection conn) throws SQLException, NamingException {

        String sqlQuery = "insert into NEO_77I8IO0F4PQ8TZ67A28RD0L2L.NODES(NODE_ID, NODE_TYPE) values(?,?)";

        PreparedStatement pstmt = conn.prepareStatement(sqlQuery);

        pstmt.setString(1, id);
        pstmt.setString(2, type);

        pstmt.executeQuery();

    }

    public static void writeNode(String id, String type) throws SQLException, NamingException {

        Connection conn = PersistManager.newConnection();
        writeNode(id, type, conn);
        conn.close();

    }

    public static void writeRelation(String childId, String parentId, Connection conn) throws SQLException, NamingException {

        String sqlQuery = "insert into NEO_77I8IO0F4PQ8TZ67A28RD0L2L.NODE__NODE(CHILD_ID, PARENT_ID) values(?,?)";

        PreparedStatement pstmt = conn.prepareStatement(sqlQuery);

        pstmt.setString(1, childId);
        pstmt.setString(2, parentId);

        pstmt.executeQuery();

    }

    public static void writeRelation(String childId, String parentId) throws SQLException, NamingException {

        Connection conn = PersistManager.newConnection();
        writeRelation(childId, parentId, conn);
        conn.close();

    }

    public static void writeSensorNode(String id, String type, Connection conn) throws SQLException, NamingException {

        String sqlQuery = "insert into NEO_77I8IO0F4PQ8TZ67A28RD0L2L.SENSOR_NODES(SN_ID, SENSOR_TYPE) values(?,?)";

        PreparedStatement pstmt = conn.prepareStatement(sqlQuery);

        pstmt.setString(1, id);
        pstmt.setString(2, type);

        pstmt.executeQuery();

    }

    public static void writeSensorNode(String id, String type) throws SQLException, NamingException {

        Connection conn = PersistManager.newConnection();
        writeSensorNode(id, type, conn);
        conn.close();

    }

    public static void writeEvaluableNode(String id, String expr, Class clazz, Connection conn) throws SQLException, NamingException {

        String sqlQuery = "";
        if (clazz == ArithmeticalNode.class) {
            sqlQuery = "insert into NEO_77I8IO0F4PQ8TZ67A28RD0L2L.ARITHMETICAL_NODES(AN_ID, EXPR) values(?,?)";
        }
        else if (clazz == LogicalNode.class) {
            sqlQuery = "insert into NEO_77I8IO0F4PQ8TZ67A28RD0L2L.LOGICAL_NODES(LN_ID, EXPR) values(?,?)";
        }

        PreparedStatement pstmt = conn.prepareStatement(sqlQuery);

        pstmt.setString(1, id);
        pstmt.setString(2, expr);

        pstmt.executeQuery();
    }

    public static void writeArithmeticalNode(String id, String expr, Connection conn) throws SQLException, NamingException {
        writeEvaluableNode(id, expr, ArithmeticalNode.class, conn);
    }

    public static void writeArithmeticalNode(String id, String expr) throws SQLException, NamingException {
        Connection conn = PersistManager.newConnection();
        writeEvaluableNode(id, expr, ArithmeticalNode.class, conn);
        conn.close();
    }

    public static void writeLogicalNode(String id, String expr, Connection conn) throws SQLException, NamingException {
        writeEvaluableNode(id, expr, LogicalNode.class, conn);
    }

    public static void writeLogicalNode(String id, String expr) throws SQLException, NamingException {
        Connection conn = PersistManager.newConnection();
        writeEvaluableNode(id, expr, LogicalNode.class, conn);
        conn.close();
    }

    public static void writeEventNode(String id, String importance, String msg, Connection conn) throws SQLException, NamingException {

        String sqlQuery = "insert into NEO_77I8IO0F4PQ8TZ67A28RD0L2L.EVENT_NODES(EN_ID, IMPORTANCE, MESSAGE) values(?,?,?)";

        PreparedStatement pstmt = conn.prepareStatement(sqlQuery);

        pstmt.setString(1, id);
        pstmt.setString(2, importance);
        pstmt.setString(2, msg);

        pstmt.executeQuery();
    }

    public static void writeEventNode(String id, String importance, String msg) throws SQLException, NamingException {

        Connection conn = PersistManager.newConnection();
        writeEventNode(id, importance, msg, conn);
        conn.close();
    }

    private static void deleteConfig() throws SQLException, NamingException {
        Connection conn = PersistManager.newConnection();

        conn.createStatement().execute("delete from NEO_77I8IO0F4PQ8TZ67A28RD0L2L.EVENT_NODES");
        conn.createStatement().execute("delete from NEO_77I8IO0F4PQ8TZ67A28RD0L2L.LOGICAL_NODES");
        conn.createStatement().execute("delete from NEO_77I8IO0F4PQ8TZ67A28RD0L2L.ARITHMETICAL_NODES");
        conn.createStatement().execute("delete from NEO_77I8IO0F4PQ8TZ67A28RD0L2L.SENSOR_NODES");
        conn.createStatement().execute("delete from NEO_77I8IO0F4PQ8TZ67A28RD0L2L.NODE__NODE");
        conn.createStatement().execute("delete from NEO_77I8IO0F4PQ8TZ67A28RD0L2L.NODES");

        conn.close();
    }


    public static List<Node> getNodes() {
        // тут, допустим, есть код...
        return null;
    }

    private static Object getParameters(String nodeId, Class clazz) {

        try (Connection conn = PersistManager.newConnection()) {

            if (clazz == SensorNode.class) {
                String sqlQuery = "select * from NEO_77I8IO0F4PQ8TZ67A28RD0L2L.SENSOR_NODES where SN_ID = ?";

                PreparedStatement pstmt = conn.prepareStatement(sqlQuery);

                pstmt.setString(1, nodeId);

                ResultSet resultSet = pstmt.executeQuery();

                if (resultSet.next()) {

                    SensorNode sn = new SensorNode();
                    sn.setSensorId(resultSet.getLong("SENSOR_ID"));

                    return sn;

                } else {
                    return new Timestamp(0);
                }

            }


        } catch (SQLException | NamingException e) {

            e.printStackTrace();
        }

        return null;
    }


    public static void createObjectsFromConfig() {

        // тут без магии никак

        List<Node> fromDB = getNodes();

        for (Node node : fromDB) {

            switch (node.getType()) {
                case "sensor_node":
                    SensorNode sn = new SensorNode();
                    sn.setId(node.getId());
                    sn.setType(node.getType());

                    SensorNode params = (SensorNode) getParameters(sn.getId(), SensorNode.class);

                    sn.setSensorId(params.getSensorId());
                    // add to Sensor Stack


                    sensorNodes.add(sn);
                    // add to all nodes
                    allNodes.add(sn);
                    break;
                case "arithmetical_node":
                    ArithmeticalNode an = new ArithmeticalNode();
                    // get parameters from database for this node
                    // ... get id, get type
                    // set parameters for base class and child class
                    // ... set id, set type
                    // add to Arithmetical Stack
                    arithmeticalNodes.add(an);
                    // add to all nodes

                default:
                    break;
            }
        }
    }
}
