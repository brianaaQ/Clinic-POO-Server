package org.example.entities;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "patients")
@PrimaryKeyJoinColumn(name = "id")
public class Patient extends User {
    //creare tabela si coloane pentru baza de date
    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    private String phone;

    @Column(columnDefinition = "TEXT")
    private String address;

    @Column(columnDefinition = "TEXT")
    private String allergies;

    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Appointment> appointments;

    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<MedicalRecord> medicalHistory;

    //constructori
    public Patient() {
        super();
        this.userType = UserType.PATIENT;
        this.appointments = new ArrayList<>();
        this.medicalHistory = new ArrayList<>();
    }

    public Patient(String email, String firstName, String lastName,
            LocalDate dateOfBirth, String phone, String address, String allergies) {
        super(email, firstName, lastName, UserType.PATIENT);
        this.dateOfBirth = dateOfBirth;
        this.phone = phone;
        this.address = address;
        this.allergies = allergies;
        this.appointments = new ArrayList<>();
        this.medicalHistory = new ArrayList<>();
    }

    //afisare dupa login
    @Override
    public String getDisplayName() {
        return firstName + " " + lastName;
    }

    @Override
    public String getRole() {
        return "PATIENT";
    }

    //operatii specifice pentru pacienti
    public Appointment requestAppointment(Doctor doctor, java.time.LocalDateTime appointmentDate, boolean hasReferral) {
        if (doctor.isRequiresReferral() && !hasReferral) {
            throw new IllegalArgumentException("This doctor requires a referral");
        }

        Appointment appointment = new Appointment();
        appointment.setPatient(this);
        appointment.setDoctor(doctor);
        appointment.setAppointmentDate(appointmentDate);
        appointment.setHasReferral(hasReferral);
        appointment.setStatus(Appointment.AppointmentStatus.SCHEDULED);

        return appointment;
    }

    // Getteri si setter1
    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAllergies() {
        return allergies;
    }

    public void setAllergies(String allergies) {
        this.allergies = allergies;
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
            appointment.setPatient(this);
        }
    }

    public List<MedicalRecord> getMedicalHistory() {
        return new ArrayList<>(medicalHistory);
    }

    public void setMedicalHistory(List<MedicalRecord> medicalHistory) {
        this.medicalHistory = medicalHistory;
    }

    public void addMedicalRecord(MedicalRecord record) {
        if (record != null) {
            this.medicalHistory.add(record);
            record.setPatient(this);
        }
    }

    @Override
    public String toString() {
        return getDisplayName() + " (Patient)";
    }
}