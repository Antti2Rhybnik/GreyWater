package com.greywater.iot.rest;

import com.greywater.iot.config.ConfigManager;
import com.greywater.iot.config.SaveConfigException;
import com.greywater.iot.gwcontext.GWContext;
import com.greywater.iot.jpa.*;
import com.greywater.iot.nodeNetwork.ArithmeticalNode;
import com.greywater.iot.nodeNetwork.NodeMaster;
import com.greywater.iot.nodeNetwork.SensorNode;
import com.greywater.iot.persistence.PersistManager;

import javax.inject.Singleton;
import javax.naming.NamingException;
import javax.persistence.*;
import javax.script.Compilable;
import javax.script.CompiledScript;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.ws.rs.*;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/*Это основной инструмент общения с веб клиентом.
* Но на данный момент это свалка. Мы используем это для вывода информации для системы
* Можете не присматриваться к наполнению. Важно знать, что здесь мы можем наладить общение с веб клиентом.*/
@Path("api")
@Singleton
public class RestApiProvider {

    public RestApiProvider() {
    }


    @POST
    @Path("saveNodes")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response saveNodesConfig(String config) {
        System.out.println("saveNodeConfig begin");

        try {
            ConfigManager.saveConfig(config);
        } catch (SaveConfigException e) {
            e.printStackTrace();
            return Response.ok(e.getMessage()).status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }

        System.out.println("saveNodeConfig end");
        return Response.ok("saved").build();
    }

    @POST
    @Path("storeConfig")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response storeConfig(String config) {

        try {

            ConfigManager.storeConfig(config);
            NodeMaster.constructed = false;

        } catch (RandomServerException e) {
            e.printStackTrace();
            return Response.ok(e.getMessage()).status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }

        return Response.ok("saved").build();
    }



    @GET
    @Path("sensorsInfo")
    @Produces(MediaType.APPLICATION_JSON)
    public List<SensorNode> getSensorsInfo() {
        return NodeMaster.sensorNodes;
    }

    @GET
    @Path("sensorValues")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Message> getSensorValues(@QueryParam("id") Long id, @QueryParam("limit") Integer limit) {

        return Message.getMessages(id, limit);
    }

    @GET
    @Path("getNodeState")
    @Produces(MediaType.TEXT_PLAIN)
    public String getNodeState(@QueryParam("id") String id) {

        String state = "";
        for (ArithmeticalNode an: NodeMaster.arithmeticalNodes) {
            if (an.getId().equals(id)) {
                state = an.getState().toString();
            }
        }

        return state;
    }


    @GET
    @Path("getLastEvent")
    @Produces(MediaType.TEXT_PLAIN)
    public String getLastEvent() {

        return Message.getLastEvent();
    }

    @GET
    @Path("getEvent")
    @Produces(MediaType.TEXT_PLAIN)
    public String getEvent(@QueryParam("node_id") String node_id) {

        return Message.getEvent(node_id);
    }

    @GET
    @Path("getNodes")
    @Produces(MediaType.APPLICATION_JSON)
    public Response test() {
        return Response.ok(ConfigManager.getConfig()).build();
    }

    @GET
    @Path("loadConfig")
    @Produces(MediaType.APPLICATION_JSON)
    public Response loadConfig() {
        return Response.ok(ConfigManager.loadConfig()).build();
    }




    @GET
    @Path("stop")
    @Produces(MediaType.APPLICATION_JSON)
    public Response stop() {

        NodeMaster.stop();
        System.out.println("STOPPED!!!");

        return Response.ok("stopped").build();
    }

    @GET
    @Path("start")
    @Produces(MediaType.APPLICATION_JSON)
    public Response start() {

        try {
            NodeMaster.start();
            System.out.println("STARTED!!!");
            return Response.ok("started").build();
        } catch (RandomServerException e) {
            return Response.ok(e.getMessage()).build();
        }
    }
}
