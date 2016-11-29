package com.greywater.iot.nodeNetwork;

/**
 * Created by alexander on 28.11.16.
 */
import com.greywater.iot.persistence.PersistManager;

import javax.naming.NamingException;
import javax.script.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;




public class Node<T> {

    private String id;
    List<Node> inputs;
    T state;
    String type;

    Node() {}

    Node(List<Node> inputs) {
        this.inputs = inputs;
    }


    public void addInput(Node n) {
        if (inputs == null) inputs = new ArrayList<>();
        if (!inputs.contains(n)) {
            inputs.add(n);
        }
    }
    public static String getTypeFromDb(String id){
        String sqlQuery = "SELECT NODE_TYPE FROM NODES WHERE NODE_ID = ?";
        try(Connection conn = PersistManager.newConnection()){
            PreparedStatement pstmt = conn.prepareStatement(sqlQuery);
            pstmt.setString(1, id);
            ResultSet resultSet = pstmt.executeQuery();
            if (resultSet.next()) {
                pstmt.close();
                return resultSet.getString(0);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (NamingException e) {
            e.printStackTrace();
        }
        return null;
    }


    void setInputs(List<Node> inputs) {
        this.inputs = inputs;
    }

    T getState() {
        return state;
    }

    String getId() {
        return id;
    }

    public String getType() { return type; }


}
