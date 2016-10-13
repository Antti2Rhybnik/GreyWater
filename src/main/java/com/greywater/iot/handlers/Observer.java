package com.greywater.iot.handlers;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by alexander on 10/13/16.
 */
public class Observer implements Runnable {
    public static Date recentlyActiveDate = new Date();

    @Override
    public void run() {
        Calendar calendar = Calendar.getInstance();
        Date now = calendar.getTime();
        EntityManager entityManager = Persistence.createEntityManagerFactory("GreyWater").createEntityManager();




    }
}
