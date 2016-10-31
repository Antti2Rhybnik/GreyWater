package com.greywater.iot.jpa;

import com.greywater.iot.persistence.PersistManager;

import javax.naming.NamingException;
import javax.xml.bind.annotation.XmlRootElement;
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
    private String sensorId;
    private Double sensorValue;

    public String getgDevice() {
        return gDevice;
    }

    public void setgDevice(String gDevice) {
        this.gDevice = gDevice;
    }

    public void setgCreated(Timestamp gCreated) {
        this.gCreated = gCreated;
    }

    public String getSensorId() {
        return sensorId;
    }

    public void setSensorId(String sensorId) {
        this.sensorId = sensorId;
    }

    public Double getSensorValue() {
        return sensorValue;
    }

    public void setSensorValue(Double sensorValue) {
        this.sensorValue = sensorValue;
    }

    //Возвращает лист сообщений, пришедших после Timestamp t
    public static List<Message> getAfterTime(Timestamp t) throws SQLException, NamingException {
        List<Message> msgList = new ArrayList<>();

        String sqlQuery = "SELECT * FROM MESSAGES_TABLE WHERE G_CREATED > ?";

        Connection conn = PersistManager.newConnection();
        PreparedStatement pstmt = conn.prepareStatement(sqlQuery);
        pstmt.setTimestamp(1, t);
        ResultSet resultSet = pstmt.executeQuery();

        while (resultSet.next()) {

            Message msg = new Message();
            msg.setgDevice(resultSet.getString("G_DEVICE"));
            msg.setgCreated(resultSet.getDate("G_CREATED"));
            msg.setSensorId(resultSet.getString("SENSOR_ID"));
            msg.setSensorValue(resultSet.getDouble("SENSOR_VALUE"));

            msgList.add(msg);
        }

        conn.close();
        return msgList;
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

        } catch (SQLException | NamingException ex) {

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
