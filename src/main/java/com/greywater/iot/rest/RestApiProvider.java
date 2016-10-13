package com.greywater.iot.rest;

import com.greywater.iot.jpa.MessagesTableEntity;
import com.greywater.iot.jpa.Sensor;
import com.greywater.iot.jpa.Thing;

import javax.inject.Singleton;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.sql.DataSource;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by antti on 09.10.16.
 */


@Path("api")
@Singleton
public class RestApiProvider {

    DataSource ds = null;
    EntityManager em = null;


    @GET
    @Path("test")
    @Produces(MediaType.TEXT_PLAIN)
    public String getTestString() {
        return "it works";
    }


    @GET
    @Path("sensor")
    @Produces(MediaType.APPLICATION_JSON)
    public String getSensor(@QueryParam("id") String id) {

        MessagesTableEntity s = em.find(MessagesTableEntity.class, id);
        return s.toString();
    }

    @GET
    @Path("sensors")
    @Produces(MediaType.APPLICATION_JSON)
    public List<MessagesTableEntity> getAllSensors() {
        em = Persistence.createEntityManagerFactory("GreyWater").createEntityManager();
        List<MessagesTableEntity> list = em.createNamedQuery("getAll", MessagesTableEntity.class).getResultList();
        em.close();
        return list;
    }

    @GET
    @Path("pers")
    @Produces(MediaType.APPLICATION_JSON)
    public String persist() {

        em = Persistence.createEntityManagerFactory("GreyWater").createEntityManager();

        Sensor s1 = new Sensor();
        Sensor s2 = new Sensor();
        Sensor s3 = new Sensor();
        Thing t1 = new Thing();
        Thing t2 = new Thing();

        s1.setId(1);
        s1.setType("WATER");
        s2.setId(2);
        s2.setType("LIGHT");
        s3.setId(3);
        s3.setType("WATER");

        s1.setThing(t1);
        s2.setThing(t1);


        s3.setThing(t2);

        List<Sensor> ss1 = new ArrayList<>();
        ss1.add(s1);
        ss1.add(s2);
        List<Sensor> ss2 = new ArrayList<>();
        ss2.add(s3);

        t1.setSensors(ss1);
        t1.setId(1);
        t1.setName("Бутылка");

        t2.setSensors(ss2);
        t2.setId(2);
        t2.setName("Труба");

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




    public RestApiProvider() {

        if (ds == null || em == null) try {
            InitialContext ctx = new InitialContext();
            ds = (DataSource) ctx.lookup("java:comp/env/jdbc/DefaultDB");
//            em = Persistence.createEntityManagerFactory("GreyWater").createEntityManager();





        } catch (NamingException e) {
            e.getMessage();
        }
    }




}
