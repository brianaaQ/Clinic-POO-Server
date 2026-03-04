package org.example.dao.impl;

import org.example.dao.AppointmentDAO;
import org.example.database.BaseRepository;
import org.example.database.HibernateUtil;
import org.example.entities.Appointment;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
//CRUD
public class AppointmentDAOImpl extends BaseRepository<Appointment> implements AppointmentDAO {
    //constructor
    public AppointmentDAOImpl() {
        super(Appointment.class);
    }

    //metode ce cauta in baza de date datele cerute si returneaza o lista cu acestea
    //gaseste progamarile unui pacient
    @Override
    public List<Appointment> findByPatientId(Long patientId) {
        String jpql = "SELECT a FROM Appointment a WHERE a.patient.id = ?1 ORDER BY a.appointmentDate";
        return executeQuery(jpql, patientId);
    }

    //cauta programarile unui doctor
    @Override
    public List<Appointment> findByDoctorId(Long doctorId) {
        String jpql = "SELECT a FROM Appointment a WHERE a.doctor.id = ?1 ORDER BY a.appointmentDate";
        return executeQuery(jpql, doctorId);
    }

    //cauta toate programarile unui docotr dupa o anumita data
    @Override
    public List<Appointment> findByDoctorIdAndDate(Long doctorId, LocalDate date) {
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(23, 59, 59);
        
        String jpql = "SELECT a FROM Appointment a WHERE a.doctor.id = ?1 AND a.appointmentDate BETWEEN ?2 AND ?3 ORDER BY a.appointmentDate";
        return executeQuery(jpql, doctorId, startOfDay, endOfDay);
    }
    
    @Override
    public List<Appointment> findByDateRange(LocalDateTime start, LocalDateTime end) {
        String jpql = "SELECT a FROM Appointment a WHERE a.appointmentDate BETWEEN ?1 AND ?2 ORDER BY a.appointmentDate";
        return executeQuery(jpql, start, end);
    }

    //verifica daca un doctor are liber in intervalul orar (al zilei respective) mentionat
    @Override
    public boolean isTimeSlotAvailable(Long doctorId, LocalDateTime appointmentDate) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            String jpql = "SELECT COUNT(a) FROM Appointment a WHERE a.doctor.id = ?1 AND a.appointmentDate = ?2 AND a.status = 'SCHEDULED'";
            TypedQuery<Long> query = em.createQuery(jpql, Long.class);
            query.setParameter(1, doctorId);
            query.setParameter(2, appointmentDate);
            
            Long count = query.getSingleResult();
            return count == 0;
        } finally {
            em.close();
        }
    }
} 