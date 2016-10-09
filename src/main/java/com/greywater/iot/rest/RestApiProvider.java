package com.greywater.iot.rest;

import com.greywater.iot.jpa.SensorsTableEntity;
import com.greywater.iot.jpa.VirtualSensor;

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

        SensorsTableEntity s = em.find(SensorsTableEntity.class, id);
        return s.toString();
    }

    @GET
    @Path("sensors")
    @Produces(MediaType.APPLICATION_JSON)
    public String getAllSensors() {

        List<SensorsTableEntity> list = (List<SensorsTableEntity>)em.createNamedQuery("SensorsTableEntity.getAll").getResultList();
        return list.toString();
    }

    @GET
    @Path("virtualsensors")
    @Produces(MediaType.APPLICATION_JSON)
    public String getAllVirtualSensors(){
        List<VirtualSensor> list = (List<VirtualSensor>) em.createNamedQuery("VirtualSensors.getAll").getResultList();
        return list.toString();
    }

    public RestApiProvider() {

        if (ds == null || em == null) try {
            InitialContext ctx = new InitialContext();
            ds = (DataSource) ctx.lookup("java:comp/env/jdbc/DefaultDB");
            em = Persistence.createEntityManagerFactory("GreyWater").createEntityManager();
        } catch (NamingException e) {
            e.getMessage();
        }
    }




}
