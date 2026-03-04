package org.example.dao;

import org.example.entities.Prescription;
import java.util.List;
import java.util.Optional;

public interface PrescriptionDAO {
    Long save(Prescription prescription);
    Optional<Prescription> findById(Long id);
    List<Prescription> findAll();
    List<Prescription> findByMedicalRecordId(Long medicalRecordId);
    List<Prescription> findByPatientId(Long patientId);
    void update(Prescription prescription);
    void delete(Long id);
} 