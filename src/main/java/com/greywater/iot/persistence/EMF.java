package com.greywater.iot.persistence;

//@WebListener
//public class EMF implements ServletContextListener {
//
//    private static EntityManagerFactory emf;
//
//    @Override
//    public void contextInitialized(ServletContextEvent event) {
//        emf = Persistence.createEntityManagerFactory("GreyWater");
//    }
//
//    @Override
//    public void contextDestroyed(ServletContextEvent event) {
//
////        emf.close();
//    }
//
//    public static EntityManager createEntityManager() {
//        if (emf == null) {
//            throw new IllegalStateException("Context is not initialized yet.");
//        }
//
//        return emf.createEntityManager();
//    }
//
//}
