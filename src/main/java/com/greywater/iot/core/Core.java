package com.greywater.iot.core;

import com.greywater.iot.jpa.SensorEntity;
import com.greywater.iot.core.predicates.GreatThanPredicate;
import com.greywater.iot.core.predicates.LessThanPredicate;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@WebListener
public class Core implements ServletContextListener {

    public static ExecutorService es;

    // Классы ядра: список сенсоров - в каждом список соответствующих предикатов
    public static List<Sensor> allSensors = new ArrayList<>();

    public static void coreInit() {

        System.out.println("Core Initialization...");
        // ИНИЦИАЛИЗАЦИЯ ЯДРА:
        // мы получаем параметры с бд и инициализируем нужные классы
        // TODO: код сумбурный и требует доработки

        // получаем сенсоры из бд - TODO: переделал бы, но пока так
        try {
            SensorEntity.getAll().forEach(se -> {

                System.out.println(se);
                Sensor s = new Sensor(se.getId());

                se.getPredicateEntities().forEach(pe -> {
                    switch (pe.getType()) {
                        case "GT":
                            s.addPredicate(new GreatThanPredicate(s, pe.getValue()));
                            break;
                        case "LT":
                            s.addPredicate(new LessThanPredicate(s, pe.getValue()));
                            break;
                    }
                });

                allSensors.add(s);
            });

        } catch (RuntimeException re) {
            re.printStackTrace();
        }

        System.out.println("Core Initialization... Done.");

    }

    @Override
    public void contextInitialized(ServletContextEvent sce) {

        es = Executors.newSingleThreadExecutor();
        coreInit();
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }
}
