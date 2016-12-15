package com.greywater.iot.gwcontext;

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


public class GWContext implements ServletContextListener {
    private static ScheduledExecutorService scheduler;
    private static ScheduledExecutorService handlersScheduler;
    private static ExecutorService msgDistribExecutor;

    public static void init() {
        try {

            // инициализация списка виртуальных сенсоров



        } catch (Exception ex) {
            ex.printStackTrace();
            System.exit(-1);
        }
    }

    @Override
    public void contextInitialized(ServletContextEvent sce) {

    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

        scheduler.shutdown();
        msgDistribExecutor.shutdown();
    }




    public static ExecutorService getMsgDistribExecutor() {
        return msgDistribExecutor;
    }



}
