package com.greywater.iot.handlers;

import com.greywater.iot.jpa.VirtualSensor;
//import org.eclipse.persistence.internal.jpa.transaction.EntityTransactionWrapper;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

/**
 * Created by alexander on 10/10/16.
 */
public class InserIntoVirtualSensor implements Runnable{
    @Override
    public void run() {
        VirtualSensor virtualSensor = new VirtualSensor();
        virtualSensor.setSensorValue(1.2);
        virtualSensor.setgDevice("azazazs");
        try {
            InitialContext ctx = new InitialContext();
            EntityManager entityManager = Persistence.createEntityManagerFactory("GreyWater").createEntityManager();
            EntityTransaction transaction = entityManager.getTransaction();
            transaction.begin();
            entityManager.persist(virtualSensor);
            transaction.commit();
            entityManager.close();
        } catch (NamingException e) {
            e.printStackTrace();
        }

    }
}
