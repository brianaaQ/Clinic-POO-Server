package org.example.entities;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "doctors")
@PrimaryKeyJoinColumn(name = "id")
public class Doctor extends User {
    //creare tabela si coloane
    @Column(nullable = false)
    private String specialization;

    @Column(name = "requires_referral")
    private boolean requiresReferral;

    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Appointment> appointments;

    //constructor
    public Doctor() {
        super();
        this.userType = UserType.DOCTOR;
        this.appointments = new ArrayList<>();
    }

    //constructor
    public Doctor(String email, String firstName, String lastName,
            String specialization, boolean requiresReferral) {
        super(email, firstName, lastName, UserType.DOCTOR);
        this.specialization = specialization;
        this.requiresReferral = requiresReferral;
        this.appointments = new ArrayList<>();
    }


    @Override
    public String getDisplayName() {
        return "Dr. " + firstName + " " + lastName;
    }

    //pt separarea utilizatorilor in baza de date
    @Override
    public String getRole() {
        return "DOCTOR";
    }

    // operatii posibile doar pt doctori
    public MedicalRecord createMedicalRecord(Patient patient, String diagnosis, String notes) {
        MedicalRecord record = new MedicalRecord();
        record.setPatient(patient);
        record.setDoctor(this);
        record.setDiagnosis(diagnosis);
        record.setNotes(notes);
        return record;
    }

    public Prescription createPrescription(MedicalRecord record, String medication,
            String dosage, String duration, String instructions) {
        Prescription prescription = new Prescription();
        prescription.setMedicalRecord(record);
        prescription.setMedication(medication);
        prescription.setDosage(dosage);
        prescription.setDuration(duration);
        prescription.setInstructions(instructions);
        return prescription;
    }

    public Treatment createTreatment(MedicalRecord record, String treatmentName,
            String description) {
        Treatment treatment = new Treatment();
        treatment.setMedicalRecord(record);
        treatment.setTreatmentName(treatmentName);
        treatment.setDescription(description);
        return treatment;
    }

    // Getteri si setteri
    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        if (specialization == null || specialization.trim().isEmpty()) {
            throw new IllegalArgumentException("Specialization cannot be empty");
        }
        this.specialization = specialization;
    }

    public boolean isRequiresReferral() {
        return requiresReferral;
    }

    public void setRequiresReferral(boolean requiresReferral) {
        this.requiresReferral = requiresReferral;
    }

    public List<Appointment> getAppointments() {
        return new ArrayList<>(appointments);
    }

    public void setAppointments(List<Appointment> appointments) {
        this.appointments = appointments;
    }

    public void addAppointment(Appointment appointment) {
        if (appointment != null) {
            this.appointments.add(appointment);
            appointment.setDoctor(this);
        }
    }

    @Override
    public String toString() {
        return getDisplayName() + " (" + specialization + ")";
    }
}