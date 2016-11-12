package com.greywater.iot.rest;

import com.greywater.iot.gwcontext.GWContext;
import com.greywater.iot.jpa.*;
import com.greywater.iot.persistence.PersistManager;
import com.greywater.iot.utils.AwesomeHTMLBuilder;
import com.greywater.iot.vsensors.Aggregator;

import javax.inject.Singleton;
import javax.persistence.*;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
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
    @Path("setConfig")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response setConfig(List<VirtualSensor> virtualSensorList) {

        // TODO: добавить проверку на корректность ссылок в пришёдших сущностях
        // TODO: добавить проверку нет ли сущностей в БД, повторяющих пришедшие
        // TODO: добавить корректный persist или merge, сделав референсы на другие объекты


        System.out.println("setConfig begin");

        try {
            EntityManager em = PersistManager.newEntityManager();

            for (VirtualSensor vs : virtualSensorList) {
                em.getTransaction().begin();
                em.merge(vs);
                em.getTransaction().commit();
            }
            em.close();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }

        System.out.println("setConfig end");
        return Response.ok("configured").build();
    }

    @GET
    @Path("test")
    @Produces(MediaType.TEXT_PLAIN)
    public String getTestString() {

        return "-21";
    }

    @GET
    @Path("sensorValues")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSensorValues(@QueryParam("id") String id, @QueryParam("limit") Integer limit) {

        List<VirtualMessage> messages = new ArrayList<>();

        try {

            EntityManager em = PersistManager.newEntityManager();
            TypedQuery<VirtualMessage> q = em.createNamedQuery("VirtualMessage.getLastNMessages", VirtualMessage.class);
            q.setParameter("1", id);
            q.setParameter("2", limit);
            messages = q.getResultList();
            em.close();

        } catch (Exception e) {
            System.err.println("Exception");
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }

        return Response.ok(messages).build();
    }

    @GET
    @Path("sensorEvents")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSensorEvents(@QueryParam("id") String id, @QueryParam("limit") Integer limit) {

        List<Event> messages = new ArrayList<>();

        try {

            EntityManager em = PersistManager.newEntityManager();
            TypedQuery<Event> query = em.createNamedQuery("Event.getLastNEvents", Event.class);
            query.setParameter("1", id);
            query.setParameter("2", limit);
            messages = query.getResultList();
            em.close();

        } catch (Exception e) {
            System.err.println("Exception");
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }

        return Response.ok(messages).build();
    }

    @GET
    @Path("aggregationTypes")
    @Produces(MediaType.APPLICATION_JSON)
    public String getAggregationTypes() {

        return "[{\"aggregation\" : \"redirect\"}, {\"aggregation\": \"multiply\"}]";
    }

    @GET
    @Path("rawsensors")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Sensor> getRawSensors() {
        return Sensor.getAll();
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

        GWContext.init();
        System.out.println("STARTED!!!");

        return Response.ok("started").build();
    }

    @POST
    @Path("json")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_JSON)
    public void json(List<VirtualSensor> vsensors) {

        System.out.println(vsensors);
    }

}
