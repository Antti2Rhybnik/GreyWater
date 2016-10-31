package com.greywater.iot.gwcontext;

import com.greywater.iot.handlers.ThresholdHandler;
import com.greywater.iot.jpa.Sensor;
import com.greywater.iot.jpa.VirtualSensor;
import com.greywater.iot.persistence.PersistManager;

import javax.ejb.EJBMetaData;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
/**
 * Тут содержатся инстансы классов, которые нужны для работы всего:
 * пулы потоков, списки сенсоров, коннекшны к бд
 */

@WebListener
public class GWContext implements ServletContextListener {
    private static ScheduledExecutorService scheduler;
    private static ScheduledExecutorService handlersScheduler;
    private static ExecutorService msgDistribExecutor;
    private static List<Sensor> allSensors;
    private static List<VirtualSensor> allVirtualSensors;

    private static void init() {
        try {

            initConfig();
            // инициализация списка виртуальных сенсоров
            allVirtualSensors = VirtualSensor.getAll();
            allSensors = new ArrayList<>();

            // создание списка сырых сенсоров
            allVirtualSensors.forEach(vs -> {
                vs.getSensors().forEach(s -> {
                    if (!allSensors.contains(s))
                        allSensors.add(s);
                });
            });

            // вывод списка виртуальных сенсоров и сырых, которые в них аггрегируются
            allVirtualSensors.forEach(vs -> {
                System.out.println(vs);
                vs.getSensors().forEach(s -> {
                    System.out.println(" --> " + s);
                });
            });
            // вывод списка сырых сенсоров и виртуальных, в которых они участвуют
            allSensors.forEach(s -> {
                System.out.println(s);
                s.getVirtualSensors().forEach(vs -> {
                    System.out.println(" --> " + vs);
                });
            });


            scheduler = Executors.newSingleThreadScheduledExecutor();
            msgDistribExecutor = Executors.newSingleThreadExecutor();

            // запуск обзёрвера
            scheduler.scheduleAtFixedRate(new Observer(), 0, 2, TimeUnit.SECONDS);

            //Запуск всех обработчиков
            scheduler.scheduleAtFixedRate(new ThresholdHandler(), 5, 20, TimeUnit.SECONDS);

        } catch (Exception ex) {
            ex.printStackTrace();
            System.exit(-1);
        }
    }

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        init();
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

        scheduler.shutdown();
        msgDistribExecutor.shutdown();
    }


    public static List<VirtualSensor> getAllVirtualSensors() {
        return allVirtualSensors;
    }

    public static ExecutorService getMsgDistribExecutor() {
        return msgDistribExecutor;
    }

    public static List<Sensor> getAllSensors() {
        return allSensors;
    }

    // добавляет в таблицу два сырых сенсора, три виртуальных и параметры
    private static void initConfig() {

        VirtualSensor vs1 = new VirtualSensor();
        VirtualSensor vs2 = new VirtualSensor();
        VirtualSensor vs3 = new VirtualSensor();

        Sensor s1 = new Sensor();
        Sensor s2 = new Sensor();

        vs1.addSensor(s1);
        vs2.addSensor(s2);
        vs3.addSensor(s1);
        vs3.addSensor(s2);

        s1.addVirtualSensor(vs1);
        s1.addVirtualSensor(vs3);
        s2.addVirtualSensor(vs2);
        s2.addVirtualSensor(vs3);

        s1.setId("1");
        s2.setId("2");

        vs1.setId("1");
        vs2.setId("2");
        vs3.setId("3");

        s1.setType("LUMINOSITY");
        s2.setType("HUMIDITY");

        vs1.setAggregationType("redirect");
        vs2.setAggregationType("redirect");
        vs3.setAggregationType("multiply");

        EntityManager em = PersistManager.newEntityManager();
        EntityTransaction tr = em.getTransaction();

        tr.begin();
        em.merge(vs1);
        em.merge(vs2);
        em.merge(vs3);
        tr.commit();

        em.close();

    }
}
