package com.greywater.iot.rest;

import com.greywater.iot.gwcontext.GWContext;
import com.greywater.iot.jpa.Message;
import com.greywater.iot.jpa.Sensor;
import com.greywater.iot.jpa.Thing;
import com.greywater.iot.jpa.VirtualSensor;
import com.greywater.iot.persistence.PersistManager;
import com.greywater.iot.utils.AwesomeHTMLBuilder;

import javax.inject.Singleton;
import javax.persistence.*;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
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

    @POST
    @Path("setSensors")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_JSON)
    public String setSensor(List<VirtualSensor> virtualSensorList) {

        System.out.println("setSensor begin");

        try {
            EntityManager em = PersistManager.newEntityManager();

            for (VirtualSensor vs : virtualSensorList) {
                em.getTransaction().begin();
                em.merge(vs);
                em.getTransaction().commit();
            }
            em.close();
        } catch (IllegalArgumentException e) {
            return "IllegalArgumentException";
        } catch (IllegalStateException e) {
            return "IllegalStateException";
        } catch (TransactionRequiredException e) {
            return "TransactionRequiredException";
        } catch (RuntimeException e) {
            return "RuntimeException";
        } catch (Exception e) {
            return "Exception";
        }

        System.out.println("setSensor end");
        return "Successfully request";
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

        List<Message> messages = new ArrayList<>();

        try {
            EntityManager em = PersistManager.newEntityManager();
            TypedQuery<Message> q = em.createNamedQuery("Message.lastN", Message.class);
            q.setParameter("1", id);
            q.setParameter("2", N);
            messages = q.getResultList();
            em.close();
        } catch (IllegalArgumentException e) {
            System.err.println("IllegalArgumentException");
        } catch (IllegalStateException e) {
            System.err.println("IllegalStateException");
        } catch (QueryTimeoutException e) {
            System.err.println("QueryTimeoutException");
        } catch (TransactionRequiredException e) {
            System.err.println("TransactionRequiredException");
        } catch (PessimisticLockException e) {
            System.err.println("PessimisticLockException");
        } catch (LockTimeoutException e) {
            System.err.println("LockTimeoutException");
        } catch (PersistenceException e) {
            System.err.println("PersistenceException");
        } catch (RuntimeException e) {
            System.err.println("RunTimeException");
        } catch (Exception e) {
            System.err.println("Exception");
        }

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
    @Path("stop")
    @Produces(MediaType.APPLICATION_JSON)
    public String stop() {

        GWContext.stop();
        System.out.println("STOPPED!!!");

        return "ok";
    }

    @GET
    @Path("start")
    @Produces(MediaType.APPLICATION_JSON)
    public String start() {

        GWContext.init();
        System.out.println("STARTED!!!");

        return "ok";
    }


    @POST
    @Path("json")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_JSON)
    public void json(List<VirtualSensor> vsensors) {

        System.out.println(vsensors.get(0));
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
