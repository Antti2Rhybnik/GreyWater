package com.greywater.iot.handlers;

import com.greywater.iot.jpa.Sensor;
import com.greywater.iot.jpa.VirtualSensor;
import com.greywater.iot.vsensors.Multiplicator;
import com.greywater.iot.vsensors.SimpleRedirector;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.util.List;
import java.util.concurrent.*;

//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
/*Основной инструмент запуска обработчиков. Все обработчики реализуют интерфейс
* Runnable и запускаются строго через этот класс*/
@WebListener
public class GWContext implements ServletContextListener {

    private static ScheduledExecutorService observerScheduler;
    private static ScheduledExecutorService handlersScheduler;
    private static Executor msgDistribExecutor;
    private static List<Sensor> allSensors;
    private static List<VirtualSensor> allVirtualSensors;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        init();
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        observerScheduler.shutdownNow();
    }


    private static void init() {

        // инициализация списка сенсоров
        allSensors = Sensor.getAll();
        allVirtualSensors = VirtualSensor.getAll();
        for (VirtualSensor vs: allVirtualSensors) {
            switch (vs.getAggregationType()) {
                case "SIMPLE_REDIRECTOR":
                    vs.setVirtualSensorAggregator(new SimpleRedirector(vs));
                    break;
                case "MULTIPLICATOR":
                    vs.setVirtualSensorAggregator(new Multiplicator(vs));
                    break;
                default:
                    System.err.println("Unsupported aggregation type");
            }
        }

        msgDistribExecutor = Executors.newSingleThreadExecutor();

        // запуск обзёрвера
        observerScheduler = Executors.newSingleThreadScheduledExecutor();
        observerScheduler.scheduleAtFixedRate(new Observer(), 9, 2, TimeUnit.SECONDS);

        // TODO: добавить инициализацию планировщика обработчиков (handlersScheduler) в зависимости от количества обработчиков
        handlersScheduler = Executors.newScheduledThreadPool(0);
        //handlersScheduler.scheduleAtFixedRate()
    }

    static Executor getMsgDistribExecutor() {
        return msgDistribExecutor;
    }

    static List<Sensor> getAllSensors() {
        return allSensors;
    }

}
