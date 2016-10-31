package com.greywater.iot.gwcontext;

import com.greywater.iot.handlers.ThresholdHandler;
import com.greywater.iot.jpa.Sensor;
import com.greywater.iot.jpa.VirtualSensor;

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
}
