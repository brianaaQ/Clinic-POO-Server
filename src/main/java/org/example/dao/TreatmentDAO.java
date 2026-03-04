package org.example.dao;

import org.example.entities.Treatment;
import java.util.List;
import java.util.Optional;

public interface TreatmentDAO {
    Long save(Treatment treatment);
    Optional<Treatment> findById(Long id);
    List<Treatment> findAll();
    List<Treatment> findByMedicalRecordId(Long medicalRecordId);
    List<Treatment> findByPatientId(Long patientId);
    List<Treatment> findActiveByPatientId(Long patientId);
    void update(Treatment treatment);
    void delete(Long id);
} 