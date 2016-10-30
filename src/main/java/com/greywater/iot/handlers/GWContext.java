package com.greywater.iot.handlers;

import com.greywater.iot.jpa.Sensor;
import com.greywater.iot.jpa.VirtualSensor;
import com.greywater.iot.vsensors.Multiplicator;
import com.greywater.iot.vsensors.SimpleRedirector;

import javax.naming.InitialContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.sql.DataSource;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
/*Основной инструмент запуска обработчиков. Все обработчики реализуют интерфейс
* Runnable и запускаются строго через этот класс*/
@WebListener
public class GWContext implements ServletContextListener {


    @Override
    public void contextInitialized(ServletContextEvent sce) {
        init();
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

        observerScheduler.shutdown();
        msgDistribExecutor.shutdown();
    }


    private static void init() {
        try {

            ctx = new InitialContext();
            dataSource = (DataSource) ctx.lookup("java:comp/env/jdbc/DefaultDB");
            connection= dataSource.getConnection();

            // инициализация списка виртуальных сенсоров
            allVirtualSensors = VirtualSensor.getAll();
            allSensors = new ArrayList<>();

            // создание списка сырых сенсоров
            allVirtualSensors.forEach(vs -> {
                vs.getSensors().forEach(s-> {
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


            // запуск обзёрвера
            observerScheduler.scheduleAtFixedRate(new Observer(), 0, 1, TimeUnit.SECONDS);


            // TODO: добавить инициализацию планировщика обработчиков (handlersScheduler) в зависимости от количества обработчиков
            //handlersScheduler.scheduleAtFixedRate()

        } catch (Exception ex) {
            ex.printStackTrace();
            System.exit(-1);
        }
    }

    static ExecutorService  getMsgDistribExecutor() {
        return msgDistribExecutor;
    }

    static List<Sensor> getAllSensors() {
        return allSensors;
    }

    public static Connection getConnection() {
        return connection;
    }


    private static final ScheduledExecutorService observerScheduler = Executors.newSingleThreadScheduledExecutor();
    private static final ScheduledExecutorService handlersScheduler = Executors.newScheduledThreadPool(0);
    private static final ExecutorService  msgDistribExecutor = Executors.newSingleThreadExecutor();
    private static List<Sensor> allSensors;
    private static List<VirtualSensor> allVirtualSensors;
    private static InitialContext ctx;
    private static DataSource dataSource;
    private static Connection connection;



}
