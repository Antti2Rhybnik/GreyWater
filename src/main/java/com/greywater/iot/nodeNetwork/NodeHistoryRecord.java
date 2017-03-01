package com.greywater.iot.nodeNetwork;


import javax.xml.bind.annotation.XmlRootElement;
import java.sql.Timestamp;

@XmlRootElement
final public class NodeHistoryRecord {

    private String id;
    private String node_id;
    private String node_type;
    private Timestamp t;
    private Double value;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNode_id() {
        return node_id;
    }

    public void setNodeId(String node_id) {
        this.node_id = node_id;
    }

    public String getNode_type() {
        return node_type;
    }

    public void setNodeType(String node_type) {
        this.node_type = node_type;
    }

    public Timestamp getT() {
        return t;
    }

    public void setT(Timestamp t) {
        this.t = t;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }
}
