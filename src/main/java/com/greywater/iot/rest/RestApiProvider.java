package com.greywater.iot.rest;

import com.greywater.iot.core.SensorHandler;
import com.greywater.iot.jpa.MessageEntity;
import com.greywater.iot.jpa.SensorEntity;
import com.greywater.iot.jpa.Thing;

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

@Path("api")
@Singleton
public class RestApiProvider {

    public static String tr = "-21";

    @GET
    @Path("test")
    @Produces(MediaType.TEXT_PLAIN)
    public String getTestString() {

        return SensorHandler.someThingEvent;
    }


    @GET
    @Path("lastN")
    @Produces(MediaType.APPLICATION_JSON)
    public List<MessageEntity> getLast30(@QueryParam("id") String id, @QueryParam("N") Integer N) {

        EntityManager em = Persistence.createEntityManagerFactory("GreyWater").createEntityManager();
        TypedQuery<MessageEntity> q = em.createNamedQuery("Message.lastN", MessageEntity.class);
        q.setParameter("1", id);
        q.setParameter("2", N);
        List<MessageEntity> messageEntities = q.getResultList();
        em.close();
        return messageEntities;
    }

    @GET
    @Path("allmsg")
    @Produces(MediaType.APPLICATION_JSON)
    public List<MessageEntity> getAllMessages() {
        EntityManager em = Persistence.createEntityManagerFactory("GreyWater").createEntityManager();
        List<MessageEntity> list = em.createNamedQuery("Message.getAll", MessageEntity.class).getResultList();
        em.close();
        return list;
    }

    @GET
    @Path("allsensors")
    @Produces(MediaType.APPLICATION_JSON)
    public List<SensorEntity> getAllSensors() {
        return SensorEntity.getAll();
    }





    @GET
    @Path("pers")
    @Produces(MediaType.APPLICATION_JSON)
    public String persist() {

        EntityManager em = Persistence.createEntityManagerFactory("GreyWater").createEntityManager();

        SensorEntity s1 = em.find(SensorEntity.class, 1L);
        SensorEntity s2 = em.find(SensorEntity.class, 2L);
        SensorEntity s3 = em.find(SensorEntity.class, 3L);
        Thing t1 = em.find(Thing.class, 1L);
        Thing t2 = em.find(Thing.class, 2L);

        if (s1 == null) {
            s1 = new SensorEntity();
        }
        if (s2 == null) {
            s2 = new SensorEntity();
        }
        if (s3 == null) {
            s3 = new SensorEntity();
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
