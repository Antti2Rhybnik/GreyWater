package com.greywater.iot.handlers;

import com.greywater.iot.gwcontext.GWContext;
import com.greywater.iot.jpa.Event;
import com.greywater.iot.jpa.Parameters;
import com.greywater.iot.jpa.VirtualSensor;
import com.greywater.iot.persistence.PersistManager;

import javax.persistence.EntityManager;
import java.util.Date;

/**
 * Created by alexander on 31.10.16.
 */
public class ThresholdHandler extends BaseHandler {
    @Override
    public void run() {
        for (VirtualSensor virtualSensor : GWContext.getAllVirtualSensors()) {
            Parameters parameters = virtualSensor.getParameters();

            if (virtualSensor.getLastMessage().getValue() > parameters.getMax()) {
                genEvent(Event.LOW_RANK,
                        Event.THRESHOLD_TYPE,
                        virtualSensor.getLastMessage().getValue() - parameters.getMax(),
                        virtualSensor);
            } else if (virtualSensor.getLastMessage().getValue() < parameters.getMin()) {
                genEvent(Event.LOW_RANK,
                        Event.THRESHOLD_TYPE,
                        parameters.getMin() - virtualSensor.getLastMessage().getValue(),
                        virtualSensor);
            }
        }
    }

    private void genEvent(String rank, String type, double difference, VirtualSensor virtualSensor) {
        Event event = new Event(new Date(), rank, type, difference, virtualSensor);
        EntityManager em = PersistManager.newEntityManager();
        em.getTransaction().begin();
        em.persist(event);
        em.getTransaction().commit();
        em.close();
    }
}
