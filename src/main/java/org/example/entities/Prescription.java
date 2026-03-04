package org.example.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "prescriptions")
public class Prescription {
    //creare tabela si coloane pt baza de date
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "medical_record_id", nullable = false)
    private MedicalRecord medicalRecord;
    
    @Column(nullable = false)
    private String medication;
    
    private String dosage;
    
    private String duration;
    
    @Column(columnDefinition = "TEXT")
    private String instructions;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    //constructori
    public Prescription() {
        this.createdAt = LocalDateTime.now();
    }
    
    public Prescription(MedicalRecord medicalRecord, String medication, String dosage, String duration, String instructions) {
        this();
        this.medicalRecord = medicalRecord;
        this.medication = medication;
        this.dosage = dosage;
        this.duration = duration;
        this.instructions = instructions;
    }
    
    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }
    
    // Getteri si setteri
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public MedicalRecord getMedicalRecord() { return medicalRecord; }
    public void setMedicalRecord(MedicalRecord medicalRecord) { this.medicalRecord = medicalRecord; }
    
    //getteri si pt convenienta
    public Long getMedicalRecordId() {
        return medicalRecord != null ? medicalRecord.getId() : null;
    }
    public void setMedicalRecordId(Long medicalRecordId) {
    }
    
    public String getMedication() {
        return medication;
    }

    public void setMedication(String medication) { 
        if (medication == null || medication.trim().isEmpty()) {
            throw new IllegalArgumentException("Medication cannot be empty");
        }
        this.medication = medication; 
    }
    
    public String getDosage() {
        return dosage;
    }
    public void setDosage(String dosage) {
        this.dosage = dosage;
    }
    
    public String getDuration() {
        return duration;
    }
    public void setDuration(String duration) {
        this.duration = duration;
    }
    
    public String getInstructions() {
        return instructions;
    }
    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    @Override
    public String toString() {
        return "Prescription{medication='" + medication + "', dosage='" + dosage + "', duration='" + duration + "'}";
    }
} 