package org.example.services;

import org.example.dao.impl.MedicalRecordDAOImpl;
import org.example.dao.impl.PrescriptionDAOImpl;
import org.example.dao.impl.TreatmentDAOImpl;
import org.example.dao.impl.PatientDAOImpl;
import org.example.dao.impl.DoctorDAOImpl;
import org.example.dao.impl.AppointmentDAOImpl;
import org.example.entities.MedicalRecord;
import org.example.entities.Prescription;
import org.example.entities.Treatment;
import org.example.entities.Patient;
import org.example.entities.Doctor;
import org.example.entities.Appointment;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class MedicalService {
    private final MedicalRecordDAOImpl medicalRecordDAO;
    private final PrescriptionDAOImpl prescriptionDAO;
    private final TreatmentDAOImpl treatmentDAO;
    private final PatientDAOImpl patientDAO;
    private final DoctorDAOImpl doctorDAO;
    private final AppointmentDAOImpl appointmentDAO;
    
    public MedicalService() {
        this.medicalRecordDAO = new MedicalRecordDAOImpl();
        this.prescriptionDAO = new PrescriptionDAOImpl();
        this.treatmentDAO = new TreatmentDAOImpl();
        this.patientDAO = new PatientDAOImpl();
        this.doctorDAO = new DoctorDAOImpl();
        this.appointmentDAO = new AppointmentDAOImpl();
    }
    
    // Medical Records
    public MedicalRecord createMedicalRecord(Long patientId, Long doctorId, Long appointmentId, String diagnosis, String notes) {
        // Verify patient exists
        Optional<Patient> patientOpt = patientDAO.findById(patientId);
        if (patientOpt.isEmpty()) {
            throw new IllegalArgumentException("Patient not found");
        }
        
        // Verify doctor exists
        Optional<Doctor> doctorOpt = doctorDAO.findById(doctorId);
        if (doctorOpt.isEmpty()) {
            throw new IllegalArgumentException("Doctor not found");
        }
        
        Patient patient = patientOpt.get();
        Doctor doctor = doctorOpt.get();
        MedicalRecord record = new MedicalRecord(patient, doctor, diagnosis, notes);
        
        // Set appointment if provided
        if (appointmentId != null) {
            Optional<Appointment> appointmentOpt = appointmentDAO.findById(appointmentId);
            if (appointmentOpt.isPresent()) {
                record.setAppointment(appointmentOpt.get());
            }
        }
        
        Long recordId = medicalRecordDAO.save(record);
        record.setId(recordId);
        
        return record;
    }
    
    public List<MedicalRecord> getPatientMedicalHistory(Long patientId) {
        return medicalRecordDAO.findByPatientId(patientId);
    }
    
    public List<MedicalRecord> getDoctorMedicalRecords(Long doctorId) {
        return medicalRecordDAO.findByDoctorId(doctorId);
    }
    
    public Optional<MedicalRecord> getMedicalRecord(Long recordId) {
        return medicalRecordDAO.findById(recordId);
    }
    
    public void updateMedicalRecord(MedicalRecord record) {
        medicalRecordDAO.update(record);
    }
    
    // Prescriptions
    public Prescription createPrescription(Long medicalRecordId, String medication, String dosage, String duration, String instructions) {
        // Verify medical record exists
        Optional<MedicalRecord> recordOpt = medicalRecordDAO.findById(medicalRecordId);
        if (recordOpt.isEmpty()) {
            throw new IllegalArgumentException("Medical record not found");
        }
        
        MedicalRecord medicalRecord = recordOpt.get();
        Prescription prescription = new Prescription(medicalRecord, medication, dosage, duration, instructions);
        Long prescriptionId = prescriptionDAO.save(prescription);
        prescription.setId(prescriptionId);
        
        return prescription;
    }
    
    public List<Prescription> getMedicalRecordPrescriptions(Long medicalRecordId) {
        return prescriptionDAO.findByMedicalRecordId(medicalRecordId);
    }
    
    public List<Prescription> getPatientPrescriptions(Long patientId) {
        return prescriptionDAO.findByPatientId(patientId);
    }
    
    // Treatments
    public Treatment createTreatment(Long medicalRecordId, String treatmentName, String description, LocalDate startDate, LocalDate endDate) {
        // Verify medical record exists
        Optional<MedicalRecord> recordOpt = medicalRecordDAO.findById(medicalRecordId);
        if (recordOpt.isEmpty()) {
            throw new IllegalArgumentException("Medical record not found");
        }
        
        MedicalRecord medicalRecord = recordOpt.get();
        Treatment treatment = new Treatment(medicalRecord, treatmentName, description, startDate, endDate);
        Long treatmentId = treatmentDAO.save(treatment);
        treatment.setId(treatmentId);
        
        return treatment;
    }
    
    public List<Treatment> getMedicalRecordTreatments(Long medicalRecordId) {
        return treatmentDAO.findByMedicalRecordId(medicalRecordId);
    }
    
    public List<Treatment> getPatientTreatments(Long patientId) {
        return treatmentDAO.findByPatientId(patientId);
    }
    
    public List<Treatment> getActiveTreatments(Long patientId) {
        return treatmentDAO.findActiveByPatientId(patientId);
    }
} 