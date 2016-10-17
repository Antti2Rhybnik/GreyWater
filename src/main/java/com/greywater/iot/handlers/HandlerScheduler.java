package com.greywater.iot.handlers;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by alexander on 10/10/16.
 */
@WebListener
public class HandlerScheduler implements ServletContextListener {
    public final int HANDLER_THREAD_POOL_SIZE = 30;
    private ScheduledExecutorService scheduler;

    private static Executor handlerExecutor = Executors.newFixedThreadPool(30);
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(new Observer(), 9, 2, TimeUnit.SECONDS);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        scheduler.shutdownNow();
    }
    public static Executor getHandlerExecutor() {
        return handlerExecutor;
    }

    public static void setHandlerExecutor(Executor handlerExecutor) {
        HandlerScheduler.handlerExecutor = handlerExecutor;
    }
}
