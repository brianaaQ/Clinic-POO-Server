package org.example.dao;

import org.example.entities.Patient;
import java.util.List;
import java.util.Optional;

public interface PatientDAO {
    Long save(Patient patient);
    Optional<Patient> findById(Long id);
    Optional<Patient> findByUserId(Long userId);
    List<Patient> findAll();
    void update(Patient patient);
    void delete(Long id);
} 