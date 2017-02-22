package com.greywater.iot.jpa;

import com.greywater.iot.persistence.PersistManager;

import javax.naming.NamingException;
import javax.xml.bind.annotation.DomHandler;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.rpc.holders.IntegerWrapperHolder;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import java.sql.*;

/**
 * Класс сообщений из сервиса сообщений.
 */

@XmlRootElement
public class Message implements Serializable {

    private String gDevice;
    private Date gCreated;
    private Long sensorId;
    private Double sensorValue;

    public static List<Message> lastMessages;

    public String getgDevice() {
        return gDevice;
    }

    public void setgDevice(String gDevice) {
        this.gDevice = gDevice;
    }

    public void setgCreated(Timestamp gCreated) {
        this.gCreated = gCreated;
    }

    public Long getSensorId() {
        return sensorId;
    }

    public void setSensorId(Long sensorId) {
        this.sensorId = sensorId;
    }

    public Double getSensorValue() {
        return sensorValue;
    }

    public void setSensorValue(Double sensorValue) {
        this.sensorValue = sensorValue;
    }

    public static List<Message> getMessages(Long id, Integer limit) {
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
                msg.setgCreated(resultSet.getDate("G_CREATED"));
                msg.setSensorId(resultSet.getLong("SENSOR_ID"));
                msg.setSensorValue(resultSet.getDouble("SENSOR_VALUE"));

                messages.add(msg);
            }

        }  catch (SQLException e) {
            e.printStackTrace();
        }

        return messages;
    }

    public static void updateLastMessages() {
        lastMessages = new ArrayList<>();

        String sqlQuery = "SELECT * FROM MESSAGES_TABLE ORDER BY G_CREATED DESC LIMIT 50";

        try (Connection conn = PersistManager.newConnection()) {

            PreparedStatement pstmt = conn.prepareStatement(sqlQuery);
            ResultSet resultSet = pstmt.executeQuery();

            while (resultSet.next()) {

                Message msg = new Message();
                msg.setgDevice(resultSet.getString("G_DEVICE"));
                msg.setgCreated(resultSet.getDate("G_CREATED"));
                msg.setSensorId(resultSet.getLong("SENSOR_ID"));
                msg.setSensorValue(resultSet.getDouble("SENSOR_VALUE"));

                lastMessages.add(msg);
            }

        }  catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Timestamp getLastTime() {

        try {

            System.out.println("getLastTime()");

            String sqlQuery = "SELECT G_CREATED FROM MESSAGES_TABLE ORDER BY G_CREATED DESC LIMIT 1";

            PreparedStatement pstmt = PersistManager.newConnection().prepareStatement(sqlQuery);
            ResultSet resultSet = pstmt.executeQuery();

            if (resultSet.next()) {
                return resultSet.getTimestamp("G_CREATED");
            } else {
                return new Timestamp(0);
            }

        } catch (SQLException ex) {

            return new Timestamp(0);
        }
    }


    public Date getgCreated() {
        return gCreated;
    }

    public void setgCreated(Date gCreated) {
        this.gCreated = gCreated;
    }

    public Message() {
    }

    public Double getLastValue(Integer id) {

        String sqlQuery = "SELECT SENSOR_VALUE FROM MESSAGES_TABLE WHERE SENSOR_ID = ? ORDER BY G_CREATED DESC LIMIT 1";

        Double res = 0.;

        try(Connection conn = PersistManager.newConnection()) {

            PreparedStatement pstmt = conn.prepareStatement(sqlQuery);
            pstmt.setInt(1, id);
            ResultSet resultSet = pstmt.executeQuery();

            while (resultSet.next()) {
                res = resultSet.getDouble("SENSOR_VALUE");
            }

            return res;

        } catch (SQLException e) {

            e.printStackTrace();
            return res;
        }
    }

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
        sqlQuery = "UPDATE EVENTS SET CHECK_FLAG = '1' WHERE CHECK_FLAG='0' AND EVENT_TIME=(SELECT MAX(EVENT_TIME) FROM EVENTS)";
        try(Connection conn = PersistManager.newConnection()) {

            PreparedStatement pstmt = conn.prepareStatement(sqlQuery);
            pstmt.execute();
        } catch (SQLException e) {

            e.printStackTrace();
            return res;
        }
        return res;
    }

    public static String getAllUncheckedEvent() {
        String sqlQuery = "SELECT EVENT_MESSAGE, EVENT_TIME FROM EVENTS WHERE CHECK_FLAG='0'";
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
    
    @Override
    public String toString() {
        return "Message{" +
                "gDevice='" + gDevice + '\'' +
                ", gCreated=" + gCreated +
                ", sensorId='" + sensorId + '\'' +
                ", sensorValue=" + sensorValue +
                '}';
    }

}
