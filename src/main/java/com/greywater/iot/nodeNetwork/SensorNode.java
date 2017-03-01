package com.greywater.iot.nodeNetwork;

import com.greywater.iot.jpa.Message;
import com.greywater.iot.persistence.PersistManager;

import javax.xml.bind.annotation.XmlRootElement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@XmlRootElement
public class SensorNode extends Node<Double> {

    private Long sensorId;
    private String sensorType;
    private String sensorUnit;
    private String sqlStr;

    public SensorNode() {
        super();
        type = "sensor";
    }


    SensorNode(List<Node> inputs) {
        super(inputs);
    }

//    void eval() {
//
//        for (Message m : Message.lastMessages) {
//            if (m.getSensorId().equals(sensorId)) {
//                System.out.println(sensorId);
//                state = m.getSensorValue();
//                break;
//            }
//        }
//
//        System.out.println("SensorNode " + id + ": " + state);
//
//    }

    void eval() {


        try (Connection conn = PersistManager.newConnection();
             PreparedStatement pstmt = conn.prepareStatement(sqlStr);
             ResultSet resultSet = pstmt.executeQuery()) {

            if (resultSet.next()) {
                state = resultSet.getDouble(1);
            }

        }  catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Long getSensorId() {
        return sensorId;
    }

    public void setSensorId(Long sensorId) {
        this.sensorId = sensorId;
    }

    public String getSensorType() {
        return sensorType;
    }

    public void setSensorType(String sensorType) {
        this.sensorType = sensorType;
    }

    public String getSensorUnit() {
        return sensorUnit;
    }

    public void setSensorUnit(String sensorUnit) {
        this.sensorUnit = sensorUnit;
    }

    public String getSqlStr() {
        return sqlStr;
    }

    public void setSqlStr(String sqlStr) {
        this.sqlStr = sqlStr;
    }
}
