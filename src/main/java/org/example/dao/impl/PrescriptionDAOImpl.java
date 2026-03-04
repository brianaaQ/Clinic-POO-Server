package org.example.dao.impl;

import org.example.dao.PrescriptionDAO;
import org.example.database.BaseRepository;
import org.example.entities.Prescription;

import java.util.List;
//CRUD
public class PrescriptionDAOImpl extends BaseRepository<Prescription> implements PrescriptionDAO {
    
    public PrescriptionDAOImpl() {
        super(Prescription.class);
    }

    //returneaza/cauta prescriptii pe baza unei fise medicale
    @Override
    public List<Prescription> findByMedicalRecordId(Long medicalRecordId) {
        String jpql = "SELECT p FROM Prescription p WHERE p.medicalRecord.id = ?1 ORDER BY p.createdAt DESC";
        return executeQuery(jpql, medicalRecordId);
    }

    //afiseaza/cauta prescriptiile in functie de pacientul dorit
    @Override
    public List<Prescription> findByPatientId(Long patientId) {
        String jpql = "SELECT p FROM Prescription p WHERE p.medicalRecord.patient.id = ?1 ORDER BY p.createdAt DESC";
        return executeQuery(jpql, patientId);
    }
} 