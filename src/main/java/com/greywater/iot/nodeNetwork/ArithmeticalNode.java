package com.greywater.iot.nodeNetwork;

import com.greywater.iot.persistence.PersistManager;
import com.greywater.iot.rest.RandomServerException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;


public class ArithmeticalNode extends EvaluableNode<Double> {

    private boolean integrable = false;

    public ArithmeticalNode() {
        super();
        state = 0.0;
        type = "arithmetical";
    }

    // eval method should update state
    public void eval() {

        Double result = super.evaluateScript();

        if (integrable) {
            state += result;
        } else {
            state = result;
        }

        this.storeToHistory();
        System.out.println("ArithmeticalNode " + id + ": " + state);

    }


    public boolean isIntegrable() {
        return integrable;
    }

    public void setIntegrable(boolean integrable) {
        this.integrable = integrable;
    }

    private void storeToHistory() {

        try(Connection conn = PersistManager.newConnection()) {

            String sqlQuery = "insert into NEO_77I8IO0F4PQ8TZ67A28RD0L2L.NODE_HISTORY(ID, NODE_ID, NODE_TYPE, NODE_VALUE, T) values(?,?,?,?,?)";

            PreparedStatement pstmt = conn.prepareStatement(sqlQuery);

            pstmt.setString(1, UUID.randomUUID().toString());
            pstmt.setString(2, id);
            pstmt.setString(3, type);
            pstmt.setDouble(4, state);
            pstmt.setTimestamp(5, new Timestamp(System.currentTimeMillis()));

            pstmt.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

