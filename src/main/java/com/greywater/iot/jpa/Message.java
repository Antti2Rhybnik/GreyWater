package com.greywater.iot.jpa;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import java.sql.*;

/*Класс сообщений из сервиса сообщений.
* Выглядит сложно, так что чтобы разобраться как работает почитайте сначала о JPA
* */

public class Message implements Serializable {

    private String gDevice;
    private Date gCreated;
    private long sensorId;
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

    public long getSensorId() {
        return sensorId;
    }

    public void setSensorId(long sensorId) {
        this.sensorId = sensorId;
    }

    public Double getSensorValue() {
        return sensorValue;
    }

    public void setSensorValue(Double sensorValue) {
        this.sensorValue = sensorValue;
    }

    //Возвращает лист сообщений пришедгих после Timestamp t
    public static List<Message> getAfterTime(Timestamp t) throws SQLException, NamingException
    {
        List<Message> msg_list = new ArrayList<Message>();

        String sqlQuery = "SELECT * FROM MESSAGES_TABLE WHERE G_CREATED > ?";

        InitialContext ctx = new InitialContext();
        DataSource dataSource = (DataSource) ctx.lookup("java:comp/env/jdbc/DefaultDB");
        Connection connection = dataSource.getConnection();

        PreparedStatement pstmt = connection.prepareStatement(sqlQuery);
        pstmt.setTimestamp(1, t);
        ResultSet resultSet = pstmt.executeQuery();

       while (resultSet.next())
        {
            Message msg = new Message();
            msg.setgDevice(resultSet.getString("G_DEVICE"));
            msg.setgCreated(resultSet.getDate("G_CREATED"));
            msg.setSensorId(resultSet.getLong("SENSOR_ID"));
            msg.setSensorValue(resultSet.getDouble("SENSOR_VALUE"));

            msg_list.add(msg);
        }

        return msg_list;
    }

    public Date getgCreated() {
        return gCreated;
    }

    public void setgCreated(Date gCreated) {
        this.gCreated = gCreated;
    }

    public Message() throws NamingException, SQLException
    {}

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
