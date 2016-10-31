package com.greywater.iot.persistence;



import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class PersistManager {

    private static EntityManagerFactory emf;
    private static InitialContext ctx;
    private static DataSource dataSource;

    public static EntityManager newEntityManager() {

        if (emf == null) {
            emf = Persistence.createEntityManagerFactory("GreyWater");
        }

        return emf.createEntityManager();
    }

    public static Connection newConnection() throws SQLException, NamingException {

        if (dataSource == null || ctx == null) {
            ctx = new InitialContext();
            dataSource = (DataSource) ctx.lookup("java:comp/env/jdbc/DefaultDB");
        }

        return dataSource.getConnection();
    }

}
