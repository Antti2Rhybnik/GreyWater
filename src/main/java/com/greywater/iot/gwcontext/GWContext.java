package com.greywater.iot.gwcontext;

import com.greywater.iot.jpa.Sensor;
import com.greywater.iot.jpa.VirtualSensor;

import javax.naming.InitialContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.sql.DataSource;
import java.sql.Connection;
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


            observerScheduler = Executors.newSingleThreadScheduledExecutor();
            msgDistribExecutor = Executors.newSingleThreadExecutor();

            // запуск обзёрвера
            observerScheduler.scheduleAtFixedRate(new Observer(), 0, 1, TimeUnit.SECONDS);


            // TODO: добавить инициализацию планировщика обработчиков (handlersScheduler) в зависимости от количества обработчиков
            handlersScheduler = Executors.newScheduledThreadPool(0);
            //handlersScheduler.scheduleAtFixedRate()

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

        observerScheduler.shutdown();
        msgDistribExecutor.shutdown();
    }


    static ExecutorService getMsgDistribExecutor() {
        return msgDistribExecutor;
    }

    static List<Sensor> getAllSensors() {
        return allSensors;
    }


    private static ScheduledExecutorService observerScheduler;
    private static ScheduledExecutorService handlersScheduler;
    private static ExecutorService msgDistribExecutor;
    private static List<Sensor> allSensors;
    private static List<VirtualSensor> allVirtualSensors;



}
