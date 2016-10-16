package com.greywater.iot.rest;


import com.greywater.iot.core.Core;
import com.greywater.iot.core.SensorHandler;
import com.greywater.iot.utils.SAPStruct;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("receive")
public class Receiver {

    // https://greywaterpUSER.hanatrial.ondemand.com/GreyWater/rest/receive/
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response receive(SAPStruct msg) {

        // здесь мы можем получить сенсор id и запустить что-нибудь
        Core.es.execute(new SensorHandler(msg.getSensorID()));

        return Response.ok("200").build();
    }

}
