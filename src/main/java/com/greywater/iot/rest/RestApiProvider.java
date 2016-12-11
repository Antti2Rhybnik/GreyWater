package com.greywater.iot.rest;

import com.greywater.iot.config.ConfigManager;
import com.greywater.iot.config.SaveConfigException;
import com.greywater.iot.gwcontext.GWContext;
import com.greywater.iot.jpa.*;
import com.greywater.iot.nodeNetwork.ArithmeticalNode;
import com.greywater.iot.nodeNetwork.NodeMaster;
import com.greywater.iot.persistence.PersistManager;

import javax.inject.Singleton;
import javax.naming.NamingException;
import javax.persistence.*;
import javax.script.Compilable;
import javax.script.CompiledScript;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.ws.rs.*;
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


    /**
     * На вход поступает примерно следующий джейсон:
     *
     * [{
     *   "id" : "1",
     *   "aggregationType": "redirect",
     *   "sensors": [{
     *        "id":"1",
     *        "type": "humidity"
     *   }],
     *   "things": [{
     *        "id": "1",
     *        "name": "tank"
     *   }],
     *   "parameters": {
     *        "min": 12,
     *        "max": 22
     *   }
     * }]
     *
     * Это преобразуется в:
     *
     * [VirtualSensor{
     *  id = '1',
     *  aggregationType = 'redirect',
     *  sensors = [ Sensor{ id = '1', type = 'humidity' } ],
     *  things = [ Thing{ id = '1', name = 'tank' } ],
     *  parameters = Parameters{ id = '7b5055ef-8d2b-4624-a8fb-2b19b2f08fc6', max = 22.0, min = 12.0 }
     * }]
     */


    @POST
    @Path("save_nodes_config")
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
    @Path("stop")
    @Produces(MediaType.APPLICATION_JSON)
    public Response stop() {

        GWContext.stop();
        System.out.println("STOPPED!!!");

        return Response.ok("stopped").build();
    }

    @GET
    @Path("start")
    @Produces(MediaType.APPLICATION_JSON)
    public Response start() {

        NodeMaster.init();
        System.out.println("STARTED!!!");

        return Response.ok("started").build();
    }


}
