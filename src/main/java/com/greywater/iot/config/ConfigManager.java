package com.greywater.iot.config;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.greywater.iot.nodeNetwork.*;
import com.greywater.iot.persistence.PersistManager;

import javax.naming.NamingException;
import java.io.IOException;
import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;




public class ConfigManager {


    public static void saveConfig(String config) throws SaveConfigException {

        try (Connection conn = PersistManager.newConnection()) {

            deleteConfig();

            // parse
            ObjectMapper mapper = new ObjectMapper();

            List<JsonNode> nodes = Arrays.asList(mapper.readValue(config, JsonNode[].class));

            for (JsonNode jsonNode : nodes) {

                if (!jsonNode.hasNonNull("node_id")) throw new SaveConfigException("Incorrect format. Missing or Null field 'node_id'");
                String nodeID = jsonNode.get("node_id").asText();
                System.out.println("parsed node_id: " + nodeID);//DBG
                if (!jsonNode.hasNonNull("node_type")) throw new SaveConfigException("Incorrect format. Missing or Null field 'node_type'");
                String nodeType = jsonNode.get("node_type").asText();
                System.out.println("parsed node_type: " + nodeType);//DBG

                // filling NODES table
                //writeNode(nodeID, nodeType, conn);

                // filling NODE__NODE table
                if (!jsonNode.hasNonNull("parents")) {

                    if (!nodeType.equals("sensor")) throw new SaveConfigException("Incorrect format. Missing or Null field 'parents' for node type '" + nodeType +"'");

                } else {
                    List<String> parentIDs = Arrays.asList(mapper.readValue(String.valueOf(jsonNode.get("parents")), String[].class));

                    for (String parentId : parentIDs) {
                        writeRelation(nodeID, parentId, conn);
                        System.out.print("parsed parent_id: " + parentId);
                    }
                    System.out.println();
                }

                // filling other
                if (!jsonNode.hasNonNull("parameters")) throw new SaveConfigException("Incorrect format. Missing or Null field 'parameters'");
                JsonNode params = mapper.readValue(String.valueOf(jsonNode.get("parameters")), JsonNode.class);


                switch (nodeType) {

                    case "sensor":
                        if (!jsonNode.hasNonNull("sensor_type")) throw new SaveConfigException("Incorrect format. Missing or Null field 'sensor_type' for node type '" + nodeType +"'" );
                        String sensorType = params.get("sensor_type").asText();
                        System.out.println("parsed sensor_type: " + sensorType);//DBG
                        //writeSensorNode(nodeID, sensorType, conn);
                        break;
                    case "arithmetical":
                        if (!jsonNode.hasNonNull("arithm_expr")) throw new SaveConfigException("Incorrect format. Missing or Null field 'arithm_expr' for node type '" + nodeType +"'" );
                        String arithmExpr = params.get("arithm_expr").asText();
                        System.out.println("parsed arithm_expr: " + arithmExpr);//DBG
                        if (!jsonNode.hasNonNull("arithm_integrable")) throw new SaveConfigException("Incorrect format. Missing or Null field 'arithm_integrable' for node type '" + nodeType +"'" );
                        String arithmIntegrable = params.get("arithm_integrable").asText();
                        System.out.println("parsed arithm_integrable: " + arithmIntegrable);//DBG
                        //writeArithmeticalNode(nodeID, arithmExpr, arithmIntegrable, conn);
                        break;
                    case "logical":
                        if (!jsonNode.hasNonNull("logic_expr")) throw new SaveConfigException("Incorrect format. Missing or Null field 'logic_expr' for node type '" + nodeType +"'" );
                        String logicExpr = params.get("logic_expr").asText();
                        System.out.println("parsed logic_expr: " + logicExpr);//DBG
                        //writeLogicalNode(nodeID, logicExpr, conn);
                        break;
                    case "event":
                        if (!jsonNode.hasNonNull("event_importance")) throw new SaveConfigException("Incorrect format. Missing or Null field 'event_importance' for node type '" + nodeType +"'" );
                        String importance = params.get("event_importance").asText();
                        System.out.println("parsed event_importance: " + importance);//DBG
                        if (!jsonNode.hasNonNull("event_msg")) throw new SaveConfigException("Incorrect format. Missing or Null field 'event_msg' for node type '" + nodeType +"'" );
                        String msg = params.get("event_msg").asText();
                        System.out.println("parsed event_msg: " + msg);//DBG
                        //writeEventNode(nodeID, importance, msg, conn);
                        break;

                    default:
                        break;
                }

            } // end for

        } catch (SQLException e) {
            throw new SaveConfigException("Something wrong with Database", e);
        } catch (IOException e) {
            throw new SaveConfigException("Something wrong with parsing JSON", e);
        }
    }


    private static void writeNode(String id, String type, Connection conn) throws SQLException {

        String sqlQuery = "insert into NEO_77I8IO0F4PQ8TZ67A28RD0L2L.NODES(NODE_ID, NODE_TYPE) values(?,?)";

        PreparedStatement pstmt = conn.prepareStatement(sqlQuery);

        pstmt.setString(1, id);
        pstmt.setString(2, type);

        pstmt.execute();

    }

    public static void writeNode(String id, String type) throws SQLException {

        Connection conn = PersistManager.newConnection();
        writeNode(id, type, conn);
        conn.close();

    }

    private static void writeRelation(String childId, String parentId, Connection conn) throws SQLException {

        String sqlQuery = "insert into NEO_77I8IO0F4PQ8TZ67A28RD0L2L.NODE__NODE(RECORD_ID, CHILD_ID, PARENT_ID) values(?,?,?)";

        PreparedStatement pstmt = conn.prepareStatement(sqlQuery);

        String recordId = UUID.randomUUID().toString();

        pstmt.setString(1, recordId);
        pstmt.setString(2, childId);
        pstmt.setString(3, parentId);

        pstmt.execute();

    }

    public static void writeRelation(String childId, String parentId) throws SQLException {

        Connection conn = PersistManager.newConnection();
        writeRelation(childId, parentId, conn);
        conn.close();

    }

    private static void writeSensorNode(String id, String type, Connection conn) throws SQLException {

        String sqlQuery = "insert into NEO_77I8IO0F4PQ8TZ67A28RD0L2L.SENSOR_NODES(SN_ID, SENSOR_TYPE) values(?,?)";

        PreparedStatement pstmt = conn.prepareStatement(sqlQuery);

        pstmt.setString(1, id);
        pstmt.setString(2, type);

        pstmt.execute();

    }

    public static void writeSensorNode(String id, String type) throws SQLException {

        Connection conn = PersistManager.newConnection();
        writeSensorNode(id, type, conn);
        conn.close();

    }



    private static void writeArithmeticalNode(String id, String expr, String integrable, Connection conn) throws SQLException {
        String sqlQuery =  "insert into NEO_77I8IO0F4PQ8TZ67A28RD0L2L.ARITHMETICAL_NODES(AN_ID, EXPR, INTEGRABLE) values(?,?,?)";
        PreparedStatement pstmt = conn.prepareStatement(sqlQuery);

        pstmt.setString(1, id);
        pstmt.setString(2, expr);
        pstmt.setString(3, integrable);

        pstmt.execute();
    }

    public static void writeArithmeticalNode(String id, String expr, String integrable) throws SQLException {
        Connection conn = PersistManager.newConnection();
        writeArithmeticalNode(id, expr, integrable, conn);
        conn.close();
    }

    private static void writeLogicalNode(String id, String expr, Connection conn) throws SQLException {
        String sqlQuery =  "insert into NEO_77I8IO0F4PQ8TZ67A28RD0L2L.LOGICAL_NODES(LN_ID, EXPR) values(?,?)";
        PreparedStatement pstmt = conn.prepareStatement(sqlQuery);

        pstmt.setString(1, id);
        pstmt.setString(2, expr);

        pstmt.execute();
    }

    public static void writeLogicalNode(String id, String expr) throws SQLException {
        Connection conn = PersistManager.newConnection();
        writeLogicalNode(id, expr, conn);
        conn.close();
    }

    private static void writeEventNode(String id, String importance, String msg, Connection conn) throws SQLException {

        String sqlQuery = "insert into NEO_77I8IO0F4PQ8TZ67A28RD0L2L.EVENT_NODES(EN_ID, IMPORTANCE, MESSAGE) values(?,?,?)";

        PreparedStatement pstmt = conn.prepareStatement(sqlQuery);

        pstmt.setString(1, id);
        pstmt.setString(2, importance);
        pstmt.setString(2, msg);

        pstmt.execute();
    }

    public static void writeEventNode(String id, String importance, String msg) throws SQLException {

        Connection conn = PersistManager.newConnection();
        writeEventNode(id, importance, msg, conn);
        conn.close();
    }

    private static void deleteConfig() throws SQLException {
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

        try (Connection conn = PersistManager.newConnection();
             PreparedStatement pstmt = conn.prepareStatement(sqlQuery);
             ResultSet resultSet = pstmt.executeQuery()) {

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

        }  catch (SQLException e) {
            e.printStackTrace();
        }
        return sensorNodes;
    }

    public static List<ArithmeticalNode> getArithmeticalNodes() {

        List<ArithmeticalNode> arithmeticalNodes = new ArrayList<>();

        String sqlQuery = "SELECT * FROM ARITHMETICAL_NODES";

        try (Connection conn = PersistManager.newConnection();
             PreparedStatement pstmt = conn.prepareStatement(sqlQuery);
             ResultSet resultSet = pstmt.executeQuery()) {

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

        }  catch (SQLException e) {
            e.printStackTrace();
        }
        return arithmeticalNodes;
    }

    public static List<LogicalNode> getLogicalNodes() {

        List<LogicalNode> logicalNodes = new ArrayList<>();

        String sqlQuery = "SELECT * FROM LOGICAL_NODES";

        try (Connection conn = PersistManager.newConnection();
             PreparedStatement pstmt = conn.prepareStatement(sqlQuery);
             ResultSet resultSet = pstmt.executeQuery()) {

            while (resultSet.next()) {

                LogicalNode node = new LogicalNode();
                // for Node
                node.setId(resultSet.getString("LN_ID"));
                node.setType("ARITHMETICAL_NODE");

                // for LogicalNode
                node.setExpr(resultSet.getString("EXPR"));


                logicalNodes.add(node);
            }

        }  catch (SQLException e) {
            e.printStackTrace();
        }
        return logicalNodes;
    }

    public static List<EventNode> getEventNodes() {

        List<EventNode> eventNodes = new ArrayList<>();

        String sqlQuery = "SELECT * FROM EVENT_NODES";

        try (Connection conn = PersistManager.newConnection();
             PreparedStatement pstmt = conn.prepareStatement(sqlQuery);
             ResultSet resultSet = pstmt.executeQuery();) {

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

        }  catch (SQLException e) {
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

        }  catch (SQLException e) {
            e.printStackTrace();
        }
        return parents;
    }

}
