package com.greywater.iot.core;

import com.greywater.iot.core.predicates.Predicate;
import com.greywater.iot.jpa.MessageEntity;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by antti on 17.10.16.
 */
public class Sensor {

    Integer id;
    List<Predicate> predicates = new ArrayList<>();

    public Sensor(Integer id) {
        this.id = id;
    }


    public void addPredicate(Predicate predicate) {
        predicates.add(predicate);
    }

    public List<Double> getActualValues(int limit) {
        EntityManager em = Persistence.createEntityManagerFactory("GreyWater").createEntityManager();
        TypedQuery<MessageEntity> q = em.createNamedQuery("MessageEntity.lastActualMessages", MessageEntity.class);
        q.setParameter("1", id);
        q.setParameter("2", limit);
        List<Double> list = new ArrayList<>();
        q.getResultList().forEach(me -> {
            list.add(me.getSensorValue());
        });
        em.close();
        return list;
    }

    public List<Date> getActualTimestamps(int limit) {
        EntityManager em = Persistence.createEntityManagerFactory("GreyWater").createEntityManager();
        TypedQuery<MessageEntity> q = em.createNamedQuery("MessageEntity.lastActualMessages", MessageEntity.class);
        q.setParameter("1", id);
        q.setParameter("2", limit);
        List<Date> list = new ArrayList<>();
        q.getResultList().forEach(me -> {
            list.add(me.getgCreated());
        });
        em.close();
        return list;
    }


    public Double getActualValue() {
        return getActualValues(1).get(0);
    }

    public Date getActualTimestamp() {
        return getActualTimestamps(1).get(0);
    }


    List<Predicate> getPredicates() {
        return predicates;
    }

}

