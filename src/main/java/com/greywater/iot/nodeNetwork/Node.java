package com.greywater.iot.nodeNetwork;


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

    String id;
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
        try(Connection conn = PersistManager.newConnection()) {
            PreparedStatement pstmt = conn.prepareStatement(sqlQuery);
            pstmt.setString(1, id);
            ResultSet resultSet = pstmt.executeQuery();
            if (resultSet.next()) {
                pstmt.close();
                return resultSet.getString(0);
            }
        } catch (SQLException | NamingException e) {
            e.printStackTrace();
        }
        return null;
    }


    public void setInputs(List<Node> inputs) {
        this.inputs = inputs;
    }

    public T getState() {
        return state;
    }
    public void setState(T state) {
        this.state = state;
    }


    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getType() { return type; }
    public void setType(String type) {
        this.type = type;
    }

    void eval() {};
}
