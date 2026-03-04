package org.example.dao.impl;

import org.example.dao.TreatmentDAO;
import org.example.database.BaseRepository;
import org.example.entities.Treatment;

import java.util.List;
//CRUD
public class TreatmentDAOImpl extends BaseRepository<Treatment> implements TreatmentDAO {
    
    public TreatmentDAOImpl() {
        super(Treatment.class);
    }

    //afiseaza/cauta tratamentele in functie de o fisa medicala precizata
    @Override
    public List<Treatment> findByMedicalRecordId(Long medicalRecordId) {
        String jpql = "SELECT t FROM Treatment t WHERE t.medicalRecord.id = ?1 ORDER BY t.createdAt DESC";
        return executeQuery(jpql, medicalRecordId);
    }

    //cauta/afiseaza tratamentele unui pacient
    @Override
    public List<Treatment> findByPatientId(Long patientId) {
        String jpql = "SELECT t FROM Treatment t WHERE t.medicalRecord.patient.id = ?1 ORDER BY t.createdAt DESC";
        return executeQuery(jpql, patientId);
    }

    //cauta/ afiseaza tratamentele "active" ale unui pacient
    @Override
    public List<Treatment> findActiveByPatientId(Long patientId) {
        String jpql = "SELECT t FROM Treatment t WHERE t.medicalRecord.patient.id = ?1 AND " +
                     "(t.startDate IS NULL OR t.startDate <= CURRENT_DATE) AND " +
                     "(t.endDate IS NULL OR t.endDate >= CURRENT_DATE) " +
                     "ORDER BY t.createdAt DESC";
        return executeQuery(jpql, patientId);
    }
} 