package org.example.dao.impl;

import org.example.dao.DoctorDAO;
import org.example.database.BaseRepository;
import org.example.entities.Doctor;

import java.util.List;
import java.util.Optional;
//CRUD
public class DoctorDAOImpl extends BaseRepository<Doctor> implements DoctorDAO {
    
    public DoctorDAOImpl() {
        super(Doctor.class);
    }

    //afiseaza doctorul cerut
    @Override
    public Optional<Doctor> findByUserId(Long userId) {
        String jpql = "SELECT d FROM Doctor d WHERE d.id = ?1";
        return executeSingleQuery(jpql, userId);
    }

    //afisarea tuturor doctorilor cu specializarea mentionata
    @Override
    public List<Doctor> findBySpecialization(String specialization) {
        String jpql = "SELECT d FROM Doctor d WHERE d.specialization = ?1";
        return executeQuery(jpql, specialization);
    }
} 