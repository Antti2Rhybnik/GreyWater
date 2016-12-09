package com.greywater.iot.config;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.greywater.iot.nodeNetwork.*;
import com.greywater.iot.persistence.PersistManager;

import javax.naming.NamingException;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;




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
                    String arithmIntegrable = params.get("arithm_integrable").asText();
                    writeArithmeticalNode(nodeID, arithmExpr, arithmIntegrable, conn);
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
    private static void writeNode(String id, String type, Connection conn) throws SQLException, NamingException {

        String sqlQuery = "insert into NEO_77I8IO0F4PQ8TZ67A28RD0L2L.NODES(NODE_ID, NODE_TYPE) values(?,?)";

        PreparedStatement pstmt = conn.prepareStatement(sqlQuery);

        pstmt.setString(1, id);
        pstmt.setString(2, type);

        pstmt.execute();

    }

    public static void writeNode(String id, String type) throws SQLException, NamingException {

        Connection conn = PersistManager.newConnection();
        writeNode(id, type, conn);
        conn.close();

    }

    private static void writeRelation(String childId, String parentId, Connection conn) throws SQLException, NamingException {

        String sqlQuery = "insert into NEO_77I8IO0F4PQ8TZ67A28RD0L2L.NODE__NODE(CHILD_ID, PARENT_ID) values(?,?)";

        PreparedStatement pstmt = conn.prepareStatement(sqlQuery);

        pstmt.setString(1, childId);
        pstmt.setString(2, parentId);

        pstmt.execute();

    }

    public static void writeRelation(String childId, String parentId) throws SQLException, NamingException {

        Connection conn = PersistManager.newConnection();
        writeRelation(childId, parentId, conn);
        conn.close();

    }

    private static void writeSensorNode(String id, String type, Connection conn) throws SQLException, NamingException {

        String sqlQuery = "insert into NEO_77I8IO0F4PQ8TZ67A28RD0L2L.SENSOR_NODES(SN_ID, SENSOR_TYPE) values(?,?)";

        PreparedStatement pstmt = conn.prepareStatement(sqlQuery);

        pstmt.setString(1, id);
        pstmt.setString(2, type);

        pstmt.execute();

    }

    public static void writeSensorNode(String id, String type) throws SQLException, NamingException {

        Connection conn = PersistManager.newConnection();
        writeSensorNode(id, type, conn);
        conn.close();

    }



    private static void writeArithmeticalNode(String id, String expr, String integrable, Connection conn) throws SQLException, NamingException {
        String sqlQuery =  "insert into NEO_77I8IO0F4PQ8TZ67A28RD0L2L.ARITHMETICAL_NODES(AN_ID, EXPR, INTEGRABLE) values(?,?,?)";
        PreparedStatement pstmt = conn.prepareStatement(sqlQuery);

        pstmt.setString(1, id);
        pstmt.setString(2, expr);
        pstmt.setString(3, integrable);

        pstmt.execute();
    }

    public static void writeArithmeticalNode(String id, String expr, String integrable) throws SQLException, NamingException {
        Connection conn = PersistManager.newConnection();
        writeArithmeticalNode(id, expr, integrable, conn);
        conn.close();
    }

    private static void writeLogicalNode(String id, String expr, Connection conn) throws SQLException, NamingException {
        String sqlQuery =  "insert into NEO_77I8IO0F4PQ8TZ67A28RD0L2L.LOGICAL_NODES(LN_ID, EXPR) values(?,?)";
        PreparedStatement pstmt = conn.prepareStatement(sqlQuery);

        pstmt.setString(1, id);
        pstmt.setString(2, expr);

        pstmt.execute();
    }

    public static void writeLogicalNode(String id, String expr) throws SQLException, NamingException {
        Connection conn = PersistManager.newConnection();
        writeLogicalNode(id, expr, conn);
        conn.close();
    }

    private static void writeEventNode(String id, String importance, String msg, Connection conn) throws SQLException, NamingException {

        String sqlQuery = "insert into NEO_77I8IO0F4PQ8TZ67A28RD0L2L.EVENT_NODES(EN_ID, IMPORTANCE, MESSAGE) values(?,?,?)";

        PreparedStatement pstmt = conn.prepareStatement(sqlQuery);

        pstmt.setString(1, id);
        pstmt.setString(2, importance);
        pstmt.setString(2, msg);

        pstmt.execute();
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


    public static List<SensorNode> getSensorNodes() {

        List<SensorNode> sensorNodes = new ArrayList<>();

        String sqlQuery = "SELECT * FROM SENSOR_NODES";

        try (Connection conn = PersistManager.newConnection()) {

            PreparedStatement pstmt = conn.prepareStatement(sqlQuery);
            ResultSet resultSet = pstmt.executeQuery();

            while (resultSet.next()) {

                SensorNode node = new SensorNode();
                // for Node
                node.setId(resultSet.getString("SN_ID"));
                node.setType("SENSOR_NODE");

                // for SensorNode
                node.setSensorType(resultSet.getString("SENSOR_TYPE"));
                node.setSensorId(resultSet.getLong("SENSOR_ID"));


                sensorNodes.add(node);
            }

        }  catch (SQLException | NamingException e) {
            e.printStackTrace();
        }
        return sensorNodes;
    }

    public static List<ArithmeticalNode> getArithmeticalNodes() {

        List<ArithmeticalNode> arithmeticalNodes = new ArrayList<>();

        String sqlQuery = "SELECT * FROM ARITHMETICAL_NODES";

        try (Connection conn = PersistManager.newConnection()) {

            PreparedStatement pstmt = conn.prepareStatement(sqlQuery);
            ResultSet resultSet = pstmt.executeQuery();

            while (resultSet.next()) {

                ArithmeticalNode node = new ArithmeticalNode();
                // for Node
                node.setId(resultSet.getString("AN_ID"));
                node.setType("ARITHMETICAL_NODE");

                // for ArithmeticalNode
                node.setIntegrable(resultSet.getString("INTEGRABLE").equalsIgnoreCase("true"));
                node.setExpr(resultSet.getString("EXPR"));

                arithmeticalNodes.add(node);
            }

        }  catch (SQLException | NamingException e) {
            e.printStackTrace();
        }
        return arithmeticalNodes;
    }

    public static List<LogicalNode> getLogicalNodes() {

        List<LogicalNode> logicalNodes = new ArrayList<>();

        String sqlQuery = "SELECT * FROM LOGICAL_NODES";

        try (Connection conn = PersistManager.newConnection()) {

            PreparedStatement pstmt = conn.prepareStatement(sqlQuery);
            ResultSet resultSet = pstmt.executeQuery();

            while (resultSet.next()) {

                LogicalNode node = new LogicalNode();
                // for Node
                node.setId(resultSet.getString("LN_ID"));
                node.setType("ARITHMETICAL_NODE");

                // for LogicalNode
                node.setExpr(resultSet.getString("EXPR"));


                logicalNodes.add(node);
            }

        }  catch (SQLException | NamingException e) {
            e.printStackTrace();
        }
        return logicalNodes;
    }

    public static List<EventNode> getEventNodes() {

        List<EventNode> eventNodes = new ArrayList<>();

        String sqlQuery = "SELECT * FROM EVENT_NODES";

        try (Connection conn = PersistManager.newConnection()) {

            PreparedStatement pstmt = conn.prepareStatement(sqlQuery);
            ResultSet resultSet = pstmt.executeQuery();

            while (resultSet.next()) {

                EventNode node = new EventNode();
                // for Node
                node.setId(resultSet.getString("EN_ID"));
                node.setType("EVENT_NODE");

                // for EventNode
                node.setImportance(resultSet.getString("IMPORTANCE"));
                node.setMessage(resultSet.getString("MESSAGE"));

                eventNodes.add(node);
            }

        }  catch (SQLException | NamingException e) {
            e.printStackTrace();
        }
        return eventNodes;
    }

    public static List<String> getParentsForNode(String nodeId) {

        List<String> parents = new ArrayList<>();

        String sqlQuery = "SELECT * FROM NODE__NODE WHERE CHILD_ID = ?";

        try (Connection conn = PersistManager.newConnection()) {

            PreparedStatement pstmt = conn.prepareStatement(sqlQuery);
            pstmt.setString(1, nodeId);
            ResultSet resultSet = pstmt.executeQuery();

            while (resultSet.next()) {

                parents.add(resultSet.getString("PARENT_ID"));
            }

        }  catch (SQLException | NamingException e) {
            e.printStackTrace();
        }
        return parents;
    }

}
