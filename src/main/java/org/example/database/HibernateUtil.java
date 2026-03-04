package org.example.database;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
//comunicarea cu baza de date
public class HibernateUtil {
    
    private static final EntityManagerFactory emf = buildEMF();

    private static EntityManagerFactory buildEMF() {
        try {
            return Persistence.createEntityManagerFactory("clinica-hibernate-unit");
        } catch (Exception ex) {
            System.err.println("Initial EntityManagerFactory creation failed: " + ex.getMessage());
            throw new ExceptionInInitializerError("Couldn't connect to database");
        }
    }

    public static EntityManager getEntityManager() {
        return emf.createEntityManager();
    }
    
    public static EntityManagerFactory getEntityManagerFactory() {
        return emf;
    }
    
    public static void shutdown() {
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }
} 