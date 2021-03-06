package com.greywater.iot.config;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import com.greywater.iot.nodeNetwork.*;
import com.greywater.iot.persistence.PersistManager;
import com.greywater.iot.rest.RandomServerException;

import javax.naming.NamingException;
import java.io.IOException;
import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;




public class ConfigManager {


    public static boolean isValidJSON(final String json) throws IOException {

        ObjectMapper mapper = new ObjectMapper();

        try {
            mapper.readTree(json);
        } catch(JsonProcessingException e){
            return false;
        }

        return true;
    }

    public static String loadConfig() {
        String conf = "";
        String sqlQuery = "SELECT * FROM NEO_77I8IO0F4PQ8TZ67A28RD0L2L.CONFIGS";

        try(Connection conn = PersistManager.newConnection();
            PreparedStatement pstmt = conn.prepareStatement(sqlQuery);
            ResultSet resultSet = pstmt.executeQuery()) {

                if (resultSet.next()) {
                    conf = resultSet.getString("JSON");
                }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return conf;
    }


    public static void storeConfig(String config) throws RandomServerException {

        try(Connection conn = PersistManager.newConnection()) {

            conn.createStatement().execute("delete from NEO_77I8IO0F4PQ8TZ67A28RD0L2L.CONFIGS");

            String sqlQuery = "insert into NEO_77I8IO0F4PQ8TZ67A28RD0L2L.CONFIGS(ID, JSON) values(?,?)";

            PreparedStatement pstmt = conn.prepareStatement(sqlQuery);

            pstmt.setString(1, UUID.randomUUID().toString());
            pstmt.setString(2, config);

            pstmt.execute();

        } catch (SQLException e) {
            throw new RandomServerException("Something wrong with Database", e);
        }


    }


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
                writeNode(nodeID, nodeType, conn);

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
                        if (!params.hasNonNull("sensor_type")) throw new SaveConfigException("Incorrect format. Missing or Null field 'sensor_type' for node type '" + nodeType +"'" );
                        String sensorType = params.get("sensor_type").asText();
                        System.out.println("parsed sensor_type: " + sensorType);//DBG
                        writeSensorNode(nodeID, sensorType, conn);
                        break;
                    case "arithmetical":
                        if (!params.hasNonNull("arithm_expr")) throw new SaveConfigException("Incorrect format. Missing or Null field 'arithm_expr' for node type '" + nodeType +"'" );
                        String arithmExpr = params.get("arithm_expr").asText();
                        System.out.println("parsed arithm_expr: " + arithmExpr);//DBG
                        if (!params.hasNonNull("arithm_integrable")) throw new SaveConfigException("Incorrect format. Missing or Null field 'arithm_integrable' for node type '" + nodeType +"'" );
                        String arithmIntegrable = params.get("arithm_integrable").asText();
                        System.out.println("parsed arithm_integrable: " + arithmIntegrable);//DBG
                        writeArithmeticalNode(nodeID, arithmExpr, arithmIntegrable, conn);
                        break;
                    case "logical":
                        if (!params.hasNonNull("logic_expr")) throw new SaveConfigException("Incorrect format. Missing or Null field 'logic_expr' for node type '" + nodeType +"'" );
                        String logicExpr = params.get("logic_expr").asText();
                        System.out.println("parsed logic_expr: " + logicExpr);//DBG
                        writeLogicalNode(nodeID, logicExpr, conn);
                        break;
                    case "event":
                        if (!params.hasNonNull("event_importance")) throw new SaveConfigException("Incorrect format. Missing or Null field 'event_importance' for node type '" + nodeType +"'" );
                        String importance = params.get("event_importance").asText();
                        System.out.println("parsed event_importance: " + importance);//DBG
                        if (!params.hasNonNull("event_msg")) throw new SaveConfigException("Incorrect format. Missing or Null field 'event_msg' for node type '" + nodeType +"'" );
                        String msg = params.get("event_msg").asText();
                        System.out.println("parsed event_msg: " + msg);//DBG
                        writeEventNode(nodeID, importance, msg, conn);
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
        // rewrite config
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
        List<Node> nodes = new ArrayList<>();
        String sqlQuery = "SELECT * FROM NODES";

        try (Connection conn = PersistManager.newConnection();
             PreparedStatement pstmt = conn.prepareStatement(sqlQuery);
             ResultSet resultSet = pstmt.executeQuery()) {

            while (resultSet.next()) {

                Node node = new Node();

                node.setId(resultSet.getString("NODE_ID"));
                node.setType(resultSet.getString("NODE_TYPE"));

                nodes.add(node);
            }

        }  catch (SQLException e) {
            e.printStackTrace();
        }
        return nodes;

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
                node.setType("sensor");

                // for SensorNode
                node.setSensorType(resultSet.getString("SENSOR_TYPE")); // as sensor name
                node.setSensorId(resultSet.getLong("SN_ID"));


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
                node.setType("arithmetical");

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
                node.setType("logical");

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
                node.setType("event");

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


    public static String getConfig() {

        // GET from DATABASE

        List<SensorNode> sensorNodes = ConfigManager.getSensorNodes();
        List<ArithmeticalNode> arithmeticalNodes = ConfigManager.getArithmeticalNodes();
        List<LogicalNode> logicalNodes = ConfigManager.getLogicalNodes();
        List<EventNode> eventNodes = ConfigManager.getEventNodes();


        ArrayList<Node> allNodes = new ArrayList<>();

        allNodes.addAll(sensorNodes);
        allNodes.addAll(arithmeticalNodes);
        allNodes.addAll(logicalNodes);
        allNodes.addAll(eventNodes);


        for (Node node: allNodes) {

            List<String> parentsIDs = getParentsForNode(node.getId());

            for (String parentId : parentsIDs) {

                Node nn = allNodes
                        .stream()
                        .filter((n -> parentId.equals(n.getId())))
                        .findFirst()
                        .get();

                node.addInput(nn);
            }
        }



        ObjectMapper mapper = new ObjectMapper();

        ArrayNode arrayConf = mapper.createArrayNode();

        for (Node node: allNodes) {

            ObjectNode nodeConf = mapper.createObjectNode();
            ObjectNode paramConf = mapper.createObjectNode();
            ArrayNode parentsConf = mapper.createArrayNode();

            switch(node.getType()) {
                case "sensor":
                    paramConf.put("sensor_type", ((SensorNode)node).getSensorType());
                    paramConf.put("sensor_unit", "123"); // FIXME: add in SensorNode
                    break;
                case "arithmetical":
                    paramConf.put("arithm_expr", ((ArithmeticalNode)node).getExpr());
                    paramConf.put("arithm_integrable", ((ArithmeticalNode)node).isIntegrable());
                    break;
                case "logical":
                    paramConf.put("logic_expr", ((LogicalNode)node).getExpr());
                    break;
                case "event":
                    paramConf.put("event_importance", ((EventNode)node).getImportance());
                    paramConf.put("event_msg", ((EventNode)node).getMessage());
                    break;

                default:
                    break;
            }

            nodeConf.putPOJO("parameters", paramConf);

            nodeConf.put("node_id", node.getId());
            nodeConf.put("node_type", node.getType());

            if (!node.getType().equals("sensor")) {
                for (Object parent : node.getInputs()) {
                    parentsConf.add(((Node) parent).getId());
                }
                nodeConf.putPOJO("parents", parentsConf);
            }

            arrayConf.addPOJO(nodeConf);
        }

        try {
            System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(arrayConf));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return arrayConf.toString();
    }
}
