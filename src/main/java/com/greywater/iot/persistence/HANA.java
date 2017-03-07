package com.greywater.iot.persistence;

import com.greywater.iot.jpa.Message;
import com.greywater.iot.nodeNetwork.NodeHistoryRecord;
import com.greywater.iot.rest.RandomServerException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class HANA {
    public static List<NodeHistoryRecord> nodeHisory(String nodeId, int limit) throws RandomServerException {
        List<NodeHistoryRecord> history = new ArrayList<>();

        String sqlQuery = "SELECT * FROM NODE_HISTORY WHERE NODE_ID = ? ORDER BY T DESC LIMIT ?";

        try (Connection conn = PersistManager.newConnection()) {

            PreparedStatement pstmt = conn.prepareStatement(sqlQuery);

            pstmt.setString(1, nodeId);
            pstmt.setInt(2, limit);

            ResultSet resultSet = pstmt.executeQuery();

            while (resultSet.next()) {

                NodeHistoryRecord rec = new NodeHistoryRecord();
                rec.setId(resultSet.getString("ID"));
                rec.setNodeId(resultSet.getString("NODE_ID"));
                rec.setNodeType(resultSet.getString("NODE_TYPE"));
                rec.setT(resultSet.getTimestamp("T"));
                rec.setValue(resultSet.getDouble("NODE_VALUE"));

                history.add(rec);
            }

        }  catch (SQLException e) {
            throw new RandomServerException("Something wrong with DB", e);
        }
        return history;
    }

    public static List<Message> getMessages(Long id, Integer limit) throws RandomServerException {
        List<Message> messages = new ArrayList<>();

        String sqlQuery = "SELECT * FROM MESSAGES_TABLE WHERE SENSOR_ID = ? ORDER BY G_CREATED DESC LIMIT ?";

        try (Connection conn = PersistManager.newConnection()) {

            PreparedStatement pstmt = conn.prepareStatement(sqlQuery);

            pstmt.setLong(1, id);
            pstmt.setInt(2, limit);

            ResultSet resultSet = pstmt.executeQuery();

            while (resultSet.next()) {

                Message msg = new Message();
                msg.setgDevice(resultSet.getString("G_DEVICE"));
                msg.setgCreated(resultSet.getTimestamp("G_CREATED"));
                msg.setSensorId(resultSet.getLong("SENSOR_ID"));
                msg.setSensorValue(resultSet.getDouble("SENSOR_VALUE"));

                messages.add(msg);
            }

        }  catch (SQLException e) {
            throw new RandomServerException("Something wrong with DB", e);
        }

        return messages;
    }

    //получаем последний эвент в независимости от номера ноды и флага проверки
    public static String getLastEvent() {
        String sqlQuery = "SELECT EVENT_MESSAGE FROM EVENTS ORDER BY EVENT_TIME DESC LIMIT 1";
        String res = "";
        try(Connection conn = PersistManager.newConnection()) {

            PreparedStatement pstmt = conn.prepareStatement(sqlQuery);
            ResultSet resultSet = pstmt.executeQuery();

            while (resultSet.next()) {
                res = resultSet.getString("EVENT_MESSAGE");
            }

            return res;

        } catch (SQLException e) {

            e.printStackTrace();
            return res;
        }
    }

    //получаем последний эвент определенной ноды в независимости от флага проверки
    public static String getEvent(String node_id) {
        String sqlQuery = "SELECT EVENT_MESSAGE FROM EVENTS WHERE NODE_ID = ? ORDER BY EVENT_TIME DESC LIMIT 1";
        String res = "";
        try(Connection conn = PersistManager.newConnection()) {

            PreparedStatement pstmt = conn.prepareStatement(sqlQuery);
            pstmt.setString(1, node_id);
            ResultSet resultSet = pstmt.executeQuery();

            while (resultSet.next()) {
                res = resultSet.getString("EVENT_MESSAGE");
            }

            return res;

        } catch (SQLException e) {

            e.printStackTrace();
            return res;
        }
    }

    //получаем последний записанный id в независимости от флага
    public static String getLastID() {
        String sqlQuery = "SELECT NODE_ID FROM EVENTS ORDER BY EVENT_TIME DESC LIMIT 1";
        String res = "";
        try(Connection conn = PersistManager.newConnection()) {

            PreparedStatement pstmt = conn.prepareStatement(sqlQuery);
            ResultSet resultSet = pstmt.executeQuery();

            while (resultSet.next()) {
                res = resultSet.getString("NODE_ID");
            }

            return res;

        } catch (SQLException e) {

            e.printStackTrace();
            return res;
        }
    }

    //получаем последний непроверенный эвент в независимости от номера ноды
    public static String getLastUncheckedEvent() {
        String sqlQuery = "SELECT EVENT_MESSAGE, EVENT_TIME FROM EVENTS WHERE CHECK_FLAG='0' ORDER BY EVENT_TIME DESC LIMIT 1";
        String res = "";
        try(Connection conn = PersistManager.newConnection()) {

            PreparedStatement pstmt = conn.prepareStatement(sqlQuery);
            ResultSet resultSet = pstmt.executeQuery();

            while (resultSet.next()) {
                res = resultSet.getString("EVENT_MESSAGE") + " " + resultSet.getString("EVENT_TIME");
            }

        } catch (SQLException e) {

            e.printStackTrace();
            return res;
        }
        sqlQuery = "UPDATE EVENTS SET CHECK_FLAG = '1' WHERE CHECK_FLAG='0' AND EVENT_TIME=(SELECT MAX(EVENT_TIME) FROM EVENTS WHERE CHECK_FLAG='0' ORDER BY EVENT_TIME DESC LIMIT 1)";
        try(Connection conn = PersistManager.newConnection()) {

            PreparedStatement pstmt = conn.prepareStatement(sqlQuery);
            pstmt.execute();

        } catch (SQLException e) {

            e.printStackTrace();
            return res;
        }
        return res;
    }

    //получаем все непроверенные эвенты в независимости от номера ноды
    public static String getAllUncheckedEvent() {
        String sqlQuery = "SELECT NODE_ID, EVENT_MESSAGE, EVENT_TIME FROM EVENTS WHERE CHECK_FLAG='0'";
        String res = "";
        try(Connection conn = PersistManager.newConnection()) {

            PreparedStatement pstmt = conn.prepareStatement(sqlQuery);
            ResultSet resultSet = pstmt.executeQuery();

            while (resultSet.next()) {
                res = res + resultSet.getString("NODE_ID") + " " + resultSet.getString("EVENT_MESSAGE") + " " + resultSet.getString("EVENT_TIME") + "\n";
            }

        } catch (SQLException e) {

            e.printStackTrace();
            return res;
        }
        sqlQuery = "UPDATE EVENTS SET CHECK_FLAG = '1' WHERE CHECK_FLAG='0'";
        try(Connection conn = PersistManager.newConnection()) {

            PreparedStatement pstmt = conn.prepareStatement(sqlQuery);
            pstmt.execute();

        } catch (SQLException e) {

            e.printStackTrace();
            return res;
        }
        return res;
    }

    //получаем последний непроверенный эвент в зависимости от номера ноды
    public static String getLastUncheckedEventWithID(String node_id) {
        String sqlQuery = "SELECT EVENT_MESSAGE, EVENT_TIME FROM EVENTS WHERE CHECK_FLAG='0' AND NODE_ID = ? ORDER BY EVENT_TIME DESC LIMIT 1";
        String res = "";
        try(Connection conn = PersistManager.newConnection()) {

            PreparedStatement pstmt = conn.prepareStatement(sqlQuery);
            pstmt.setString(1, node_id);
            ResultSet resultSet = pstmt.executeQuery();

            while (resultSet.next()) {
                res = resultSet.getString("EVENT_MESSAGE") + " " + resultSet.getString("EVENT_TIME");
            }

        } catch (SQLException e) {

            e.printStackTrace();
            return res;
        }
        sqlQuery = "UPDATE EVENTS SET CHECK_FLAG = '1' WHERE CHECK_FLAG='0' AND NODE_ID = ? AND EVENT_TIME=(SELECT MAX(EVENT_TIME) FROM EVENTS WHERE CHECK_FLAG='0' AND NODE_ID = ? )";
        try(Connection conn = PersistManager.newConnection()) {

            PreparedStatement pstmt = conn.prepareStatement(sqlQuery);
            pstmt.setString(1, node_id);
            pstmt.setString(2, node_id);
            pstmt.execute();

        } catch (SQLException e) {

            e.printStackTrace();
            return res;
        }
        return res;
    }

    //получаем все непроверенные эвенты в зависимости от номера ноды
    public static String getAllUncheckedEventWithID(String node_id) {
        String sqlQuery = "SELECT EVENT_MESSAGE, EVENT_TIME FROM EVENTS WHERE CHECK_FLAG='0' AND NODE_ID = ? ";
        String res = "";
        try(Connection conn = PersistManager.newConnection()) {

            PreparedStatement pstmt = conn.prepareStatement(sqlQuery);
            pstmt.setString(1, node_id);
            ResultSet resultSet = pstmt.executeQuery();

            while (resultSet.next()) {
                res = res + resultSet.getString("EVENT_MESSAGE") + " " + resultSet.getString("EVENT_TIME") + "\n";
            }

        } catch (SQLException e) {

            e.printStackTrace();
            return res;
        }
        sqlQuery = "UPDATE EVENTS SET CHECK_FLAG = '1' WHERE CHECK_FLAG='0' AND NODE_ID = ? ";
        try(Connection conn = PersistManager.newConnection()) {

            PreparedStatement pstmt = conn.prepareStatement(sqlQuery);
            pstmt.setString(1, node_id);
            pstmt.execute();

        } catch (SQLException e) {

            e.printStackTrace();
            return res;
        }
        return res;
    }



    //для теста клиента
    public static String toNull() {
        String res = "";
        String sqlQuery = "UPDATE EVENTS SET CHECK_FLAG=0";
        try(Connection conn = PersistManager.newConnection()) {

            PreparedStatement pstmt = conn.prepareStatement(sqlQuery);
            pstmt.execute();
            return res;
        } catch (SQLException e) {

            e.printStackTrace();
            return res;
        }
    }
}
