package org.example.dao;

import org.example.entities.Appointment;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
public interface AppointmentDAO {
    //liste/variabile/metode necesare pentru interactiunea obiectelor cu baza de date
    Long save(Appointment appointment);
    Optional<Appointment> findById(Long id);
    List<Appointment> findAll();
    List<Appointment> findByPatientId(Long patientId);
    List<Appointment> findByDoctorId(Long doctorId);
    List<Appointment> findByDoctorIdAndDate(Long doctorId, LocalDate date);
    List<Appointment> findByDateRange(LocalDateTime start, LocalDateTime end);
    void update(Appointment appointment);
    void delete(Long id);
    boolean isTimeSlotAvailable(Long doctorId, LocalDateTime appointmentDate);
} 