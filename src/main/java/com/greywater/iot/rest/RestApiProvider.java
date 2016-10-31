package com.greywater.iot.rest;

import com.greywater.iot.jpa.Message;
import com.greywater.iot.jpa.Sensor;
import com.greywater.iot.jpa.Thing;
import com.greywater.iot.persistence.PersistManager;
import com.greywater.iot.utils.AwesomeHTMLBuilder;

import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.util.List;

/*Это основной инструмент общения с веб клиентом.
* Но на данный момент это свалка. Мы используем это для вывода информации для системы
* Можете не присматриваться к наполнению. Важно знать, что здесь мы можем наладить общение с веб клиентом.*/
@Path("api")
@Singleton
public class RestApiProvider {

    public static String tr = "-21";

    public RestApiProvider() {
    }

    @GET
    @Path("test")
    @Produces(MediaType.TEXT_PLAIN)
    public String getTestString() {

        return tr;
    }

    @GET
    @Path("lastN")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Message> getLast30(@QueryParam("id") String id, @QueryParam("N") Integer N) {

        EntityManager em = Persistence.createEntityManagerFactory("GreyWater").createEntityManager();
        TypedQuery<Message> q = em.createNamedQuery("Message.lastN", Message.class);
        q.setParameter("1", id);
        q.setParameter("2", N);
        List<Message> messages = q.getResultList();
        em.close();
        return messages;
    }

    @GET
    @Path("allmsg")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Message> getAllMessages() {
        return null;
    }

    @GET
    @Path("allsensors")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Sensor> getAllSensors() {
        return Sensor.getAll();
    }

    @GET
    @Path("pers")
    @Produces(MediaType.APPLICATION_JSON)
    public String persist() {

        return "ok";
    }

//    @GET
//    @Path("timetest")
//    @Produces(MediaType.APPLICATION_JSON)
//    public List<Message> timeTest() {
//        return Message.getAfterTime(Observer.getCurrentTimestamp());
//    }

//    @GET
//    @Path("thresholder")
//    @Produces(MediaType.TEXT_HTML)
//    public String ok() {
//        if (ThresholdHandler.isProblemDetected()) {
//            return AwesomeHTMLBuilder.getAwesomeHtmlWithPhoto("In Threshold happened problems. Handler value - ", String.valueOf(ThresholdHandler.isProblemDetected()), "http://www.ivetta.ua/wp-content/uploads/2015/07/tom-kruz-3.jpg");
//        }
//        return AwesomeHTMLBuilder.getAwesomeHtml("Everything is ok. Handler value", String.valueOf(ThresholdHandler.isProblemDetected()), "\t#808080");
//    }

//    @GET
//    @Path("thres")
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response handlerResult() {
//        JSONObject jsonObject = new JSONObject();
//        jsonObject.put("min", String.valueOf(ThresholdHandler.getLow()));
//        jsonObject.put("max", String.valueOf(ThresholdHandler.getMax()));
//        jsonObject.put("Is problem detected", String.valueOf(ThresholdHandler.isProblemDetected()));
//        if (ThresholdHandler.getCurrentMessage() != null) {
//            if (ThresholdHandler.isProblemDetected()) {
//                jsonObject.put("state", "ALARM OUT OF RANGE");
//            } else {
//                jsonObject.put("state", "normal value");
//            }
//
//            jsonObject.put("current value", String.valueOf(ThresholdHandler.getCurrentMessage().getSensorValue()));
//
//        }
//        return Response.ok(jsonObject.toJSONString()).header("Access-Control-Allow-Credentials", "true").header("Access-Control-Allow-Origin", "*").build();
//    }

    @GET
    @Path("failureClient")
    @Produces(MediaType.TEXT_HTML)
    public String filureClient() {
        return AwesomeHTMLBuilder.getMyFailureInClient();
    }


}
