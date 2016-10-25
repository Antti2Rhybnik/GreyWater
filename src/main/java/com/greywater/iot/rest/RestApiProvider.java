package com.greywater.iot.rest;

import com.greywater.iot.jpa.Message;
import com.greywater.iot.jpa.Sensor;
import com.greywater.iot.jpa.Thing;

import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("api")
@Singleton
public class RestApiProvider {

    public static String tr = "-21";

    @GET
    @Path("test")
    @Produces(MediaType.TEXT_PLAIN)
    public String getTestString() {
        return "it works";
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





    public RestApiProvider() {}


}
