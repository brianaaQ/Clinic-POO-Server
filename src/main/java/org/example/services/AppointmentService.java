package org.example.services;

import org.example.dao.AppointmentDAO;
import org.example.dao.DoctorDAO;
import org.example.dao.PatientDAO;
import org.example.dao.impl.AppointmentDAOImpl;
import org.example.dao.impl.DoctorDAOImpl;
import org.example.dao.impl.PatientDAOImpl;
import org.example.entities.Appointment;
import org.example.entities.Doctor;
import org.example.entities.Patient;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class AppointmentService {
    private final AppointmentDAO appointmentDAO;
    private final DoctorDAO doctorDAO;
    private final PatientDAO patientDAO;
    
    public AppointmentService() {
        this.appointmentDAO = new AppointmentDAOImpl();
        this.doctorDAO = new DoctorDAOImpl();
        this.patientDAO = new PatientDAOImpl();
    }
    
    public Appointment scheduleAppointment(Long patientId, Long doctorId, LocalDateTime appointmentDate, boolean hasReferral) {
        // Validate patient exists
        Optional<Patient> patientOpt = patientDAO.findByUserId(patientId);
        if (patientOpt.isEmpty()) {
            throw new IllegalArgumentException("Patient not found");
        }
        
        // Validate doctor exists
        Optional<Doctor> doctorOpt = doctorDAO.findByUserId(doctorId);
        if (doctorOpt.isEmpty()) {
            throw new IllegalArgumentException("Doctor not found");
        }

        Patient patient = patientOpt.get();
        Doctor doctor = doctorOpt.get();
        
        // Check if doctor requires referral
        if (doctor.isRequiresReferral() && !hasReferral) {
            throw new IllegalArgumentException("This doctor requires a medical referral");
        }
        
        // Check if time slot is available
        if (!appointmentDAO.isTimeSlotAvailable(doctorId, appointmentDate)) {
            throw new IllegalArgumentException("Time slot is not available");
        }
        
        // Create and save appointment
        Appointment appointment = new Appointment(patient, doctor, appointmentDate, hasReferral);
        Long appointmentId = appointmentDAO.save(appointment);
        appointment.setId(appointmentId);
        
        return appointment;
    }
    
    public List<Appointment> getPatientAppointments(Long patientId) {
        return appointmentDAO.findByPatientId(patientId);
    }
    
    public List<Appointment> getDoctorAppointments(Long doctorId) {
        return appointmentDAO.findByDoctorId(doctorId);
    }
    
    public List<Appointment> getDoctorAppointmentsByDate(Long doctorId, LocalDate date) {
        return appointmentDAO.findByDoctorIdAndDate(doctorId, date);
    }
    
    public void cancelAppointment(Long appointmentId, Long userId) {
        Optional<Appointment> appointmentOpt = appointmentDAO.findById(appointmentId);
        if (appointmentOpt.isEmpty()) {
            throw new IllegalArgumentException("Appointment not found");
        }
        
        Appointment appointment = appointmentOpt.get();
        
        // Check if user has permission to cancel (patient or doctor)
        if (!appointment.getPatientId().equals(userId) && !appointment.getDoctorId().equals(userId)) {
            throw new IllegalArgumentException("You don't have permission to cancel this appointment");
        }
        
        appointment.cancel();
        appointmentDAO.update(appointment);
    }
    
    public void completeAppointment(Long appointmentId, Long doctorId) {
        Optional<Appointment> appointmentOpt = appointmentDAO.findById(appointmentId);
        if (appointmentOpt.isEmpty()) {
            throw new IllegalArgumentException("Appointment not found");
        }
        
        Appointment appointment = appointmentOpt.get();
        
        // Only the assigned doctor can complete the appointment
        if (!appointment.getDoctorId().equals(doctorId)) {
            throw new IllegalArgumentException("Only the assigned doctor can complete this appointment");
        }
        
        appointment.complete();
        appointmentDAO.update(appointment);
    }
    
    public void markNoShow(Long appointmentId, Long doctorId) {
        Optional<Appointment> appointmentOpt = appointmentDAO.findById(appointmentId);
        if (appointmentOpt.isEmpty()) {
            throw new IllegalArgumentException("Appointment not found");
        }
        
        Appointment appointment = appointmentOpt.get();
        
        // Only the assigned doctor can mark as no-show
        if (!appointment.getDoctorId().equals(doctorId)) {
            throw new IllegalArgumentException("Only the assigned doctor can mark this appointment as no-show");
        }
        
        appointment.markNoShow();
        appointmentDAO.update(appointment);
    }
    
    public List<Doctor> getAllDoctors() {
        return doctorDAO.findAll();
    }
    
    public List<Doctor> getDoctorsBySpecialization(String specialization) {
        return doctorDAO.findBySpecialization(specialization);
    }
    
    public boolean isTimeSlotAvailable(Long doctorId, LocalDateTime appointmentDate) {
        return appointmentDAO.isTimeSlotAvailable(doctorId, appointmentDate);
    }
} 