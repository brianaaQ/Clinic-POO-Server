package org.example.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "appointments")
public class Appointment {
    //creare tabela + coloanele dorite
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;
    
    @Column(name = "appointment_date", nullable = false)
    private LocalDateTime appointmentDate;
    
    @Enumerated(EnumType.STRING)
    private AppointmentStatus status;
    
    @Column(name = "has_referral")
    private boolean hasReferral;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    public enum AppointmentStatus {
        SCHEDULED, COMPLETED, CANCELLED, NO_SHOW
    }

    //constructor
    public Appointment() {
        this.status = AppointmentStatus.SCHEDULED;
        this.createdAt = LocalDateTime.now();
    }

    //constructor
    public Appointment(Patient patient, Doctor doctor, LocalDateTime appointmentDate, boolean hasReferral) {
        this();
        this.patient = patient;
        this.doctor = doctor;
        this.appointmentDate = appointmentDate;
        this.hasReferral = hasReferral;
    }
    
    //metode pt programari, utilizeaza parametrii de la enum
    public boolean canBeCancelled() {
        return status == AppointmentStatus.SCHEDULED && 
               appointmentDate.isAfter(LocalDateTime.now().plusHours(24));
    }
    
    public void cancel() {
        if (!canBeCancelled()) {
            throw new IllegalStateException("Appointment cannot be cancelled");
        }
        this.status = AppointmentStatus.CANCELLED;
    }
    
    public void complete() {
        if (status != AppointmentStatus.SCHEDULED) {
            throw new IllegalStateException("Only scheduled appointments can be completed");
        }
        this.status = AppointmentStatus.COMPLETED;
    }
    
    public void markNoShow() {
        if (status != AppointmentStatus.SCHEDULED) {
            throw new IllegalStateException("Only scheduled appointments can be marked as no-show");
        }
        this.status = AppointmentStatus.NO_SHOW;
    }
    
    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }
    
    // Getteri si setteri
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    
    public Patient getPatient() {
        return patient;
    }
    public void setPatient(Patient patient) {
        this.patient = patient;
    }
    
    public Doctor getDoctor() {
        return doctor;
    }
    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }
    
    //verifica daca pacientul/doctorul este null, daca nu este returneaza id ul
    public Long getPatientId() {
        return patient != null ? patient.getId() : null;
    }
    public Long getDoctorId() {
        return doctor != null ? doctor.getId() : null;
    }

    //returneaza data si ora programarii
    public LocalDateTime getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(LocalDateTime appointmentDate) { 
        if (appointmentDate == null || appointmentDate.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Appointment date cannot be in the past");
        }
        this.appointmentDate = appointmentDate; 
    }
    
    public AppointmentStatus getStatus() {
        return status;
    }
    public void setStatus(AppointmentStatus status) {
        this.status = status;
    }
    
    public boolean isHasReferral() {
        return hasReferral;
    }
    public void setHasReferral(boolean hasReferral) {
        this.hasReferral = hasReferral;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    @Override
    public String toString() {
        return "Appointment{id=" + id + ", appointmentDate=" + appointmentDate + ", status=" + status + "}";
    }
} 