package org.example.database;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;

import java.util.List;
import java.util.Optional;
//repository=> gestioneaza CRUD-ul
public abstract class BaseRepository<T> {
    
    protected final Class<T> entityClass;

    //constructor pt repository
    public BaseRepository(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    //metoda salvarea utilizatorilor
    public Long save(T entity) {
        EntityManager em = HibernateUtil.getEntityManager();
        EntityTransaction et = em.getTransaction();
        
        try {
            et.begin();
            em.persist(entity);
            et.commit();
            
            //retine id ul
            try {
                var idField = entity.getClass().getDeclaredField("id");
                idField.setAccessible(true);
                return (Long) idField.get(entity);
            } catch (Exception e) {
                return null;
            }
        } catch (Exception e) {
            if (et.isActive()) {
                et.rollback();
            }
            throw new RuntimeException("Error saving entity", e);
        } finally {
            em.close();
        }
    }

    //metoda actualizarea utilizatorilor
    public void update(T entity) {
        EntityManager em = HibernateUtil.getEntityManager();
        EntityTransaction et = em.getTransaction();
        
        try {
            et.begin();
            em.merge(entity);
            et.commit();
        } catch (Exception e) {
            if (et.isActive()) {
                et.rollback();
            }
            throw new RuntimeException("Error updating entity", e);
        } finally {
            em.close();
        }
    }

    //metoda pt cautarea/gasirea ultilizatorilor dupa id
    public Optional<T> findById(Long id) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            T entity = em.find(entityClass, id);
            return Optional.ofNullable(entity);
        } finally {
            em.close();
        }
    }

    //returneaza o lista cu toti utilizatorii din tabelul dorit
    public List<T> findAll() {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            String jpql = "SELECT e FROM " + entityClass.getSimpleName() + " e";
            TypedQuery<T> query = em.createQuery(jpql, entityClass);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    //metoda pentru stergerea utilizatorilor
    public void delete(Long id) {
        EntityManager em = HibernateUtil.getEntityManager();
        EntityTransaction et = em.getTransaction();
        
        try {
            et.begin();
            T entity = em.find(entityClass, id);
            if (entity != null) {
                em.remove(entity);
            }
            et.commit();
        } catch (Exception e) {
            if (et.isActive()) {
                et.rollback();
            }
            throw new RuntimeException("Error deleting entity", e);
        } finally {
            em.close();
        }
    }

    //evitarea scrierii aceluiasi cod pt parametrii in dao si repository
    protected List<T> executeQuery(String jpql, Object... parameters) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            TypedQuery<T> query = em.createQuery(jpql, entityClass);
            for (int i = 0; i < parameters.length; i++) {
                query.setParameter(i + 1, parameters[i]);
            }
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    //obtinerea unui singur rezultat + evitarea scrierii multiple(ca mai sus)
    protected Optional<T> executeSingleQuery(String jpql, Object... parameters) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            TypedQuery<T> query = em.createQuery(jpql, entityClass);
            for (int i = 0; i < parameters.length; i++) {
                query.setParameter(i + 1, parameters[i]);
            }
            List<T> results = query.getResultList();
            return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
        } finally {
            em.close();
        }
    }
} 