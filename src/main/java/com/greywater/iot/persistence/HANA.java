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
                msg.setgCreated(resultSet.getDate("G_CREATED"));
                msg.setSensorId(resultSet.getLong("SENSOR_ID"));
                msg.setSensorValue(resultSet.getDouble("SENSOR_VALUE"));

                messages.add(msg);
            }

        }  catch (SQLException e) {
            throw new RandomServerException("Something wrong with DB", e);
        }

        return messages;
    }
}
