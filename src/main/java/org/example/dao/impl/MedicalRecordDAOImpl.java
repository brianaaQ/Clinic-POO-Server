package org.example.dao.impl;

import org.example.dao.MedicalRecordDAO;
import org.example.database.BaseRepository;
import org.example.entities.MedicalRecord;

import java.util.List;
//CRUD
public class MedicalRecordDAOImpl extends BaseRepository<MedicalRecord> implements MedicalRecordDAO {
    
    public MedicalRecordDAOImpl() {
        super(MedicalRecord.class);
    }

    //afiseaza fisa unui anumit pacient
    @Override
    public List<MedicalRecord> findByPatientId(Long patientId) {
        String jpql = "SELECT mr FROM MedicalRecord mr WHERE mr.patient.id = ?1 ORDER BY mr.createdAt DESC";
        return executeQuery(jpql, patientId);
    }

    //afiseaza fisele facute de un anumit doctor
    @Override
    public List<MedicalRecord> findByDoctorId(Long doctorId) {
        String jpql = "SELECT mr FROM MedicalRecord mr WHERE mr.doctor.id = ?1 ORDER BY mr.createdAt DESC";
        return executeQuery(jpql, doctorId);
    }

    //returneaza toate fisele legate de o anumita programare
    @Override
    public List<MedicalRecord> findByAppointmentId(Long appointmentId) {
        String jpql = "SELECT mr FROM MedicalRecord mr WHERE mr.appointment.id = ?1 ORDER BY mr.createdAt DESC";
        return executeQuery(jpql, appointmentId);
    }
} 