package com.greywater.iot.config;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.greywater.iot.nodeNetwork.*;
import com.greywater.iot.persistence.PersistManager;
import org.eclipse.persistence.sessions.serializers.JSONSerializer;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.naming.NamingException;
import java.io.IOException;
import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;




public class ConfigManager {


    public static void saveConfig(String config) throws IOException, SQLException, NamingException {

        deleteConfig();
        // parse
        ObjectMapper mapper = new ObjectMapper();

        List<JsonNode> nodes = Arrays.asList(mapper.readValue(config, JsonNode[].class));

        Connection conn = PersistManager.newConnection();

        for (JsonNode jsonNode: nodes) {
            int count = 0;

            String nodeID = jsonNode.get("node_id").asText();
            String nodeType = jsonNode.get("node_type").asText();

            // filling NODES table
            writeNode(nodeID, nodeType, conn);

            // filling NODE__NODE table
            List<String> parentIDs = new ArrayList<String>();
            final JsonNode arrNode = jsonNode.get("parents");
            if (arrNode.isArray()) {
                for (final JsonNode objNode : arrNode) {
                    parentIDs.add(objNode.toString());
                }
            }

            for (String parentId: parentIDs) {
                writeRelation(nodeID, parentId.substring(1,parentId.length()-1), conn);
            }

            // filling other
            JSONParser parser = new JSONParser();
            Object obj_parsed = null;
            try {
                obj_parsed = parser.parse(config);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            JSONArray obj_arr = (JSONArray) obj_parsed;
            JSONObject obj = (JSONObject) obj_arr.get(count);
            JSONObject params = (JSONObject) obj.get("parameters");
            switch (nodeType) {

                case "sensor":
                    String sensorType = (String)params.get("sensor_type");
                    writeSensorNode(nodeID, sensorType, conn);
                    break;
                case "arithmetical":
                    String arithmExpr = (String)params.get("arithm_expr");
                    String arithmIntegrable = (String)params.get("arithm_integrable");
                    writeArithmeticalNode(nodeID, arithmExpr, arithmIntegrable, conn);
                    break;
                case "logical":
                    String logicExpr = (String)params.get("logic_expr");
                    writeLogicalNode(nodeID, logicExpr, conn);
                    break;
                case "event":
                    String importance = (String)params.get("event_importance");
                    String msg = (String)params.get("event_msg");
                    writeEventNode(nodeID, importance, msg, conn);
                    break;

                default: break;
            }
            ++count;
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

        String sqlQuery = "insert into NEO_77I8IO0F4PQ8TZ67A28RD0L2L.NODE__NODE(RECORD_ID, CHILD_ID, PARENT_ID) values(?,?,?)";

        PreparedStatement pstmt = conn.prepareStatement(sqlQuery);

        String recordId = UUID.randomUUID().toString();

        pstmt.setString(1, recordId);
        pstmt.setString(2, childId);
        pstmt.setString(3, parentId);

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
