package org.example.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "medical_records")
public class MedicalRecord {
    //creare tabela si coloane in baza de date
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "appointment_id")
    private Appointment appointment;
    
    @Column(columnDefinition = "TEXT")
    private String diagnosis;
    
    @Column(columnDefinition = "TEXT")
    private String notes;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @OneToMany(mappedBy = "medicalRecord", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Prescription> prescriptions;
    
    @OneToMany(mappedBy = "medicalRecord", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Treatment> treatments;

    //constructori
    public MedicalRecord() {
        this.createdAt = LocalDateTime.now();
        this.prescriptions = new ArrayList<>();
        this.treatments = new ArrayList<>();
    }
    
    public MedicalRecord(Patient patient, Doctor doctor, String diagnosis, String notes) {
        this();
        this.patient = patient;
        this.doctor = doctor;
        this.diagnosis = diagnosis;
        this.notes = notes;
    }
    
    //metode pt fisa medicala
    public void addPrescription(Prescription prescription) {
        if (prescription != null) {
            prescription.setMedicalRecord(this);
            this.prescriptions.add(prescription);
        }
    }
    
    public void addTreatment(Treatment treatment) {
        if (treatment != null) {
            treatment.setMedicalRecord(this);
            this.treatments.add(treatment);
        }
    }
    
    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }
    
    // Getteri si setteri
    public Long getId() { return id; }
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
    
    public Appointment getAppointment() {
        return appointment;
    }
    public void setAppointment(Appointment appointment) {
        this.appointment = appointment;
    }

    //egtteri si setteri pt convenienta
    public Long getPatientId() {
        return patient != null ? patient.getId() : null;
    }
    public void setPatientId(Long patientId) {
    }
    
    public Long getDoctorId() {
        return doctor != null ? doctor.getId() : null;
    }
    public void setDoctorId(Long doctorId) {
    }
    
    public Long getAppointmentId() {
        return appointment != null ? appointment.getId() : null;
    }
    public void setAppointmentId(Long appointmentId) {
    }
    
    public String getDiagnosis() {
        return diagnosis;
    }
    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }
    
    public String getNotes() {
        return notes;
    }
    public void setNotes(String notes) {
        this.notes = notes;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public List<Prescription> getPrescriptions() {
        return new ArrayList<>(prescriptions);
    }
    public void setPrescriptions(List<Prescription> prescriptions) {
        this.prescriptions = prescriptions;
    }
    
    public List<Treatment> getTreatments() {
        return new ArrayList<>(treatments);
    }
    public void setTreatments(List<Treatment> treatments) {
        this.treatments = treatments;
    }
    
    @Override
    public String toString() {
        return "MedicalRecord{id=" + id + ", diagnosis='" + diagnosis + "', createdAt=" + createdAt + "}";
    }
} 