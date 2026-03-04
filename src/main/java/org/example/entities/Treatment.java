package org.example.entities;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "treatments")
public class Treatment {
    //creare tabela si coloane pt baza de date
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "medical_record_id", nullable = false)
    private MedicalRecord medicalRecord;
    
    @Column(name = "treatment_name", nullable = false)
    private String treatmentName;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @Column(name = "start_date")
    private LocalDate startDate;
    
    @Column(name = "end_date")
    private LocalDate endDate;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    //constructori
    public Treatment() {
        this.createdAt = LocalDateTime.now();
    }
    
    public Treatment(MedicalRecord medicalRecord, String treatmentName, String description, LocalDate startDate, LocalDate endDate) {
        this();
        this.medicalRecord = medicalRecord;
        this.treatmentName = treatmentName;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
    }
    
    //metode specifice tratamentului
    public boolean isActive() {
        LocalDate today = LocalDate.now();
        return (startDate == null || !startDate.isAfter(today)) && 
               (endDate == null || !endDate.isBefore(today));
    }
    
    public boolean isCompleted() {
        return endDate != null && endDate.isBefore(LocalDate.now());
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
    
    //getteri si setteri pentru convenienta
    public Long getMedicalRecordId() {
        return medicalRecord != null ? medicalRecord.getId() : null;
    }
    public void setMedicalRecordId(Long medicalRecordId) {
    }
    
    public String getTreatmentName() {
        return treatmentName;
    }
    public void setTreatmentName(String treatmentName) { 
        if (treatmentName == null || treatmentName.trim().isEmpty()) {
            throw new IllegalArgumentException("Treatment name cannot be empty");
        }
        this.treatmentName = treatmentName; 
    }
    
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    
    public LocalDate getStartDate() {
        return startDate;
    }
    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }
    
    public LocalDate getEndDate() {
        return endDate;
    }
    public void setEndDate(LocalDate endDate) { 
        if (endDate != null && startDate != null && endDate.isBefore(startDate)) {
            throw new IllegalArgumentException("End date cannot be before start date");
        }
        this.endDate = endDate; 
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    @Override
    public String toString() {
        return "Treatment{treatmentName='" + treatmentName + "', startDate=" + startDate + ", endDate=" + endDate + "}";
    }
} 