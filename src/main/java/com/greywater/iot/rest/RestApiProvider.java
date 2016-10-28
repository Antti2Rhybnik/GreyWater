package com.greywater.iot.rest;

import com.greywater.iot.handlers.Observer;
import com.greywater.iot.jpa.Message;
import com.greywater.iot.jpa.Sensor;
import com.greywater.iot.jpa.Thing;
import com.greywater.iot.utils.AwesomeHTMLBuilder;
import org.json.simple.JSONObject;

import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
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
        EntityManager em = Persistence.createEntityManagerFactory("GreyWater").createEntityManager();
        List<Message> list = em.createNamedQuery("Message.getAll", Message.class).getResultList();
        em.close();
        return list;
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

        EntityManager em = Persistence.createEntityManagerFactory("GreyWater").createEntityManager();

        Sensor s1 = em.find(Sensor.class, 1L);
        Sensor s2 = em.find(Sensor.class, 2L);
        Sensor s3 = em.find(Sensor.class, 3L);
        Thing t1 = em.find(Thing.class, 1L);
        Thing t2 = em.find(Thing.class, 2L);

        if (s1 == null) {
            s1 = new Sensor();
        }
        if (s2 == null) {
            s2 = new Sensor();
        }
        if (s3 == null) {
            s3 = new Sensor();
        }
        if (t1 == null) {
            t1 = new Thing();
        }
        if (t2 == null) {
            t2 = new Thing();
        }


        s1.setId(1);
        s1.setType("WATER");
        s2.setId(2);
        s2.setType("LIGHT");
        s3.setId(3);
        s3.setType("WATER");


        t1.setId(1);
        t1.setName("Бутылка");


        t2.setId(2);
        t2.setName("Труба");

        t1.addSensor(s1);
        t1.addSensor(s2);
        t2.addSensor(s3);
        em.getTransaction().begin();

//        em.persist(s1);
//        em.persist(s2);
//        em.persist(s3);
        em.persist(t1);
        em.persist(t2);

        em.getTransaction().commit();

        em.close();

        return "ok";
    }

    @GET
    @Path("timetest")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Message> timeTest() {
        return Message.getAfterTime(Observer.getCurrentTimestamp());
    }

    @GET
    @Path("thresholder")
    @Produces(MediaType.TEXT_HTML)
    public String ok() {
        if (ThresholdHandler.isProblemDetected()) {
            return AwesomeHTMLBuilder.getAwesomeHtmlWithPhoto("In Threshold happened problems. Handler value - ", String.valueOf(ThresholdHandler.isProblemDetected()), "http://www.ivetta.ua/wp-content/uploads/2015/07/tom-kruz-3.jpg");
        }
        return AwesomeHTMLBuilder.getAwesomeHtml("Everything is ok. Handler value", String.valueOf(ThresholdHandler.isProblemDetected()), "\t#808080");
    }

    @GET
    @Path("thres")
    @Produces(MediaType.APPLICATION_JSON)
    public Response handlerResult() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("min", String.valueOf(ThresholdHandler.getLow()));
        jsonObject.put("max", String.valueOf(ThresholdHandler.getMax()));
        jsonObject.put("Is problem detected", String.valueOf(ThresholdHandler.isProblemDetected()));
        if (ThresholdHandler.getCurrentMessage() != null) {
            if (ThresholdHandler.isProblemDetected()) {
                jsonObject.put("state", "ALARM OUT OF RANGE");
            } else {
                jsonObject.put("state", "normal value");
            }

            jsonObject.put("current value", String.valueOf(ThresholdHandler.getCurrentMessage().getSensorValue()));

        }
//        return jsonObject.toJSONString();
        return Response.ok(jsonObject.toJSONString()).header("Access-Control-Allow-Credentials", "true").header("Access-Control-Allow-Origin", "*").build();
    }

    @GET
    @Path("failureClient")
    @Produces(MediaType.TEXT_HTML)
    public String filureClient() {
        return AwesomeHTMLBuilder.getMyFailureInClient();
    }


}
