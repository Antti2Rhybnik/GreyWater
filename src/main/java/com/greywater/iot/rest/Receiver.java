package com.greywater.iot.rest;


import com.greywater.iot.handlers.SensorHandler;
import com.greywater.iot.utils.SAPStruct;

import javax.inject.Singleton;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Path("receive")
@Singleton
public class Receiver {


    ExecutorService es;

    public Receiver() {
        es = Executors.newSingleThreadExecutor();
    }


    // https://greywaterpUSER.hanatrial.ondemand.com/GreyWater/rest/receive/
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response receive(SAPStruct msg) {

        // здесь мы можем получить сенсор id и запустить что-нибудь
        System.out.println(msg.getSensorID());
        RestApiProvider.tr = msg.getSensorID().toString();

        es.submit(new SensorHandler(msg.getSensorID()));

        return Response.ok("200").build();
    }

}
