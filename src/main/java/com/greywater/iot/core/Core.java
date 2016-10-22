package com.greywater.iot.core;

import com.greywater.iot.jpa.VSensor;
import com.greywater.iot.predicates.GreatThanPredicateDelegate;
import com.greywater.iot.predicates.LessThanPredicateDelegate;
import com.greywater.iot.vsensors.SimpleRedirect;

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

    // объекты ядра - как минимум в память должны быть подгружены
    public static List<VSensor> virtualSensors = new ArrayList<>();

    public static void coreInit() {

        System.out.println("Core Initialization...");

        try {
            VSensor.getAll().forEach(vs -> {

                switch (vs.getType()) {
                    case "SimpleRedirect":
                        vs.setvSensorDelegate(new SimpleRedirect(vs.getSensors()));
                        break;
                }

                vs.getPredicates().forEach(p -> {
                    switch (p.getType()) {
                        case "GT":
                            p.setPredicateDelegate(new GreatThanPredicateDelegate(vs, p));
                            break;
                        case "LT":
                            p.setPredicateDelegate(new LessThanPredicateDelegate(vs, p));
                            break;
                    }
                });

                virtualSensors.add(vs);
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
