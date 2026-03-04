package org.example.dao.impl;

import org.example.dao.PatientDAO;
import org.example.database.BaseRepository;
import org.example.entities.Patient;

import java.util.Optional;
//CRUD
public class PatientDAOImpl extends BaseRepository<Patient> implements PatientDAO {
    
    public PatientDAOImpl() {
        super(Patient.class);
    }

    //afiseaza/cauta un anumit pacient, in funnctie de Id
    @Override
    public Optional<Patient> findByUserId(Long userId) {
        String jpql = "SELECT p FROM Patient p WHERE p.id = ?1";
        return executeSingleQuery(jpql, userId);
    }
} 