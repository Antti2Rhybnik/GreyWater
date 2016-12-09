package com.greywater.iot.app;

import com.greywater.iot.nodeNetwork.NodeMaster;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * Created by Виталий on 09.12.2016.
 */
@WebListener
public class AppInit implements ServletContextListener {
    public void contextInitialized(ServletContextEvent event) {
        NodeMaster.init();
        System.out.println("STARTED!!!");
    }

    public void contextDestroyed(ServletContextEvent event) {
        //do on application destroy
    }
}
