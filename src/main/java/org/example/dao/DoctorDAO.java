package org.example.dao;

import org.example.entities.Doctor;
import java.util.List;
import java.util.Optional;

public interface DoctorDAO {
    Long save(Doctor doctor);
    Optional<Doctor> findById(Long id);
    Optional<Doctor> findByUserId(Long userId);
    List<Doctor> findAll();
    List<Doctor> findBySpecialization(String specialization);
    void update(Doctor doctor);
    void delete(Long id);
} 