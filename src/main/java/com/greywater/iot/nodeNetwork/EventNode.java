package com.greywater.iot.nodeNetwork;

import com.greywater.iot.jpa.Message;
import com.greywater.iot.persistence.HANA;
import com.greywater.iot.persistence.PersistManager;

import javax.naming.NamingException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class EventNode extends Node<String> {

    private String importance;
    private String message;

    public EventNode() {
        super();
        type = "event";
    }


    public void setImportance(String importance) {
        this.importance = importance;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getImportance() {
        return importance;
    }

    public String getMessage() {
        return message;
    }

    public void writeEvent(String nodeID, Connection conn) throws SQLException, NamingException {

        String sqlQuery = "insert into NEO_77I8IO0F4PQ8TZ67A28RD0L2L.EVENTS(ID, EVENT_TIME, EVENT_MESSAGE, EVENT_IMPORTANCE, NODE_ID, CHECK_FLAG) values(?,?,?,?,?,?)";

        PreparedStatement pstmt = conn.prepareStatement(sqlQuery);

        String tableID = UUID.randomUUID().toString();

        Calendar calendar = Calendar.getInstance();
        Date now = calendar.getTime();
        Timestamp currentTimestamp = new Timestamp(now.getTime());

        pstmt.setString(1, tableID);
        pstmt.setTimestamp(2, currentTimestamp);
        pstmt.setString(3, message);
        pstmt.setString(4, importance);
        pstmt.setString(5, nodeID);
        pstmt.setString(6, "0");

        pstmt.execute();
    }

     void eval() {

        inputs.forEach(node -> {
            if ((Boolean) node.getState()) try (Connection conn = PersistManager.newConnection()) {
                try {
                    String prevEvent = HANA.getEvent(node.getId());
                    if (prevEvent.compareTo(this.getMessage()) != 0) {
                        writeEvent(node.getId(), conn);
                    }
                } catch (NamingException e) {
                    e.printStackTrace();
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
            else try (Connection conn = PersistManager.newConnection()) {
                try {
                    String prevEvent = HANA.getEvent(node.getId());
                    if (prevEvent.compareTo("ok") != 0) {
                        String oldMessage = this.getMessage();
                        this.setMessage("ok");
                        writeEvent(node.getId(), conn);
                        this.setMessage(oldMessage);
                    }
                } catch (NamingException e) {
                    e.printStackTrace();
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

        System.out.println("EventNode " + id + ": " + state);
    }
}
