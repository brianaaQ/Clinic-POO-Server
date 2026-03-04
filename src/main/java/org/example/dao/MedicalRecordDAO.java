package org.example.dao;

import org.example.entities.MedicalRecord;
import java.util.List;
import java.util.Optional;

public interface MedicalRecordDAO {
    Long save(MedicalRecord medicalRecord);
    Optional<MedicalRecord> findById(Long id);
    List<MedicalRecord> findAll();
    List<MedicalRecord> findByPatientId(Long patientId);
    List<MedicalRecord> findByDoctorId(Long doctorId);
    List<MedicalRecord> findByAppointmentId(Long appointmentId);
    void update(MedicalRecord medicalRecord);
    void delete(Long id);
} 