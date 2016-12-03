package com.greywater.iot.nodeNetwork;

import javax.naming.NamingException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class EventNode extends Node<String> {


    EventNode(List<Node> inputs) {
        super(inputs);
    }

    public static void writeEvent(String id, String type, Connection conn) throws SQLException, NamingException {

        String sqlQuery = "insert into NEO_77I8IO0F4PQ8TZ67A28RD0L2L.EVENTS_TABLE(EVENT_ID, GEN_TIME, IMPORTANCE_RANK, EVENT_TYPE, DIFF) values(?,?,?,?,?)";

        PreparedStatement pstmt = conn.prepareStatement(sqlQuery);

        pstmt.setString(1, id);
        pstmt.setString(4, type);

        pstmt.executeQuery();

    }


    void eval(Connection conn) {
        inputs.forEach(node -> {
            if (node.getState() == "1") {
                try {
                    writeEvent(node.getId(), node.getType(), conn);
                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (NamingException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}



