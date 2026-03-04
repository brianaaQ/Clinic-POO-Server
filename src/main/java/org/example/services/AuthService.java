package org.example.services;

import org.example.dao.UserDAO;
import org.example.dao.DoctorDAO;
import org.example.dao.PatientDAO;
import org.example.dao.impl.UserDAOImpl;
import org.example.dao.impl.DoctorDAOImpl;
import org.example.dao.impl.PatientDAOImpl;
import org.example.entities.User;
import org.example.entities.Doctor;
import org.example.entities.Patient;

import java.time.LocalDate;
import java.util.Optional;

public class AuthService {
    private final UserDAO userDAO;
    private final DoctorDAO doctorDAO;
    private final PatientDAO patientDAO;

    public AuthService() {
        this.userDAO = new UserDAOImpl();
        this.doctorDAO = new DoctorDAOImpl();
        this.patientDAO = new PatientDAOImpl();
    }

    public User login(String email, String name) {
        Optional<User> userOpt = userDAO.findByEmailAndName(email, name);

        if (userOpt.isEmpty()) {
            throw new IllegalArgumentException("User not found with provided email and name");
        }

        User user = userOpt.get();

        // Load complete user data based on type
        if (user.getUserType() == User.UserType.DOCTOR) {
            Optional<Doctor> doctorOpt = doctorDAO.findByUserId(user.getId());
            return doctorOpt.orElse((Doctor) user);
        } else {
            Optional<Patient> patientOpt = patientDAO.findByUserId(user.getId());
            return patientOpt.orElse((Patient) user);
        }
    }

    public Doctor registerDoctor(String email, String firstName, String lastName,
            String specialization, boolean requiresReferral) {
        if (userDAO.existsByEmail(email)) {
            throw new IllegalArgumentException("Email already exists");
        }

        Doctor doctor = new Doctor(email, firstName, lastName, specialization, requiresReferral);

        // With JOINED inheritance, save only once - Hibernate handles both tables
        Long doctorId = doctorDAO.save(doctor);
        doctor.setId(doctorId);

        return doctor;
    }

    public Patient registerPatient(String email, String firstName, String lastName,
            LocalDate dateOfBirth, String phone, String address, String allergies) {
        if (userDAO.existsByEmail(email)) {
            throw new IllegalArgumentException("Email already exists");
        }

        Patient patient = new Patient(email, firstName, lastName, dateOfBirth, phone, address, allergies);

        // With JOINED inheritance, save only once - Hibernate handles both tables
        Long patientId = patientDAO.save(patient);
        patient.setId(patientId);

        return patient;
    }

    public boolean isEmailAvailable(String email) {
        return !userDAO.existsByEmail(email);
    }
}