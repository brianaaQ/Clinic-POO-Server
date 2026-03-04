package org.example.services;

import org.example.entities.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

public class ClinicService {
    private final AuthService authService;
    private final AppointmentService appointmentService;
    private final MedicalService medicalService;
    private static final String DELIMITER = "|";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public ClinicService() {
        this.authService = new AuthService();
        this.appointmentService = new AppointmentService();
        this.medicalService = new MedicalService();
    }

    public String processCommand(String command) {
        try {
            String[] parts = command.split(" ", 2);
            String action = parts[0];
            String data = parts.length > 1 ? parts[1] : "";

            return switch (action) {
                case "login" -> handleLogin(data);
                case "register_doctor" -> handleRegisterDoctor(data);
                case "register_patient" -> handleRegisterPatient(data);
                case "get_doctors" -> handleGetDoctors(data);
                case "get_doctors_by_specialization" -> handleGetDoctorsBySpecialization(data);
                case "schedule_appointment" -> handleScheduleAppointment(data);
                case "get_patient_appointments" -> handleGetPatientAppointments(data);
                case "get_doctor_appointments" -> handleGetDoctorAppointments(data);
                case "get_doctor_appointments_by_date" -> handleGetDoctorAppointmentsByDate(data);
                case "cancel_appointment" -> handleCancelAppointment(data);
                case "complete_appointment" -> handleCompleteAppointment(data);
                case "create_medical_record" -> handleCreateMedicalRecord(data);
                case "get_patient_medical_history" -> handleGetPatientMedicalHistory(data);
                case "create_prescription" -> handleCreatePrescription(data);
                case "create_treatment" -> handleCreateTreatment(data);
                case "get_patient_prescriptions" -> handleGetPatientPrescriptions(data);
                case "get_patient_treatments" -> handleGetPatientTreatments(data);
                case "check_time_slot" -> handleCheckTimeSlot(data);
                default -> createErrorResponse("Unknown command: " + action);
            };
        } catch (Exception e) {
            return createErrorResponse("Error processing command: " + e.getMessage());
        }
    }

    private String handleLogin(String data) {
        try {
            Map<String, String> params = parseParams(data);
            String email = params.get("email");
            String name = params.get("name");

            if (name == null || name.trim().isEmpty()) {
                throw new IllegalArgumentException("Name must be provided");
            }

            User user = authService.login(email, name);
            return createUserResponse(user);
        } catch (Exception e) {
            return createErrorResponse("Login failed: " + e.getMessage());
        }
    }

    private String handleRegisterDoctor(String data) {
        try {
            Map<String, String> params = parseParams(data);
            String email = params.get("email");
            String firstName = params.get("firstName");
            String lastName = params.get("lastName");
            String specialization = params.get("specialization");
            boolean requiresReferral = Boolean.parseBoolean(params.get("requiresReferral"));

            Doctor doctor = authService.registerDoctor(email, firstName, lastName, specialization, requiresReferral);
            return createUserResponse(doctor);
        } catch (Exception e) {
            return createErrorResponse("Doctor registration failed: " + e.getMessage());
        }
    }

    private String handleRegisterPatient(String data) {
        try {
            Map<String, String> params = parseParams(data);
            String email = params.get("email");
            String firstName = params.get("firstName");
            String lastName = params.get("lastName");
            LocalDate dateOfBirth = LocalDate.parse(params.get("dateOfBirth"));
            String phone = params.get("phone");
            String address = params.get("address");
            String allergies = params.get("allergies");

            Patient patient = authService.registerPatient(email, firstName, lastName, dateOfBirth, phone, address,
                    allergies);
            return createUserResponse(patient);
        } catch (Exception e) {
            return createErrorResponse("Patient registration failed: " + e.getMessage());
        }
    }

    private String handleGetDoctors(String data) {
        try {
            List<Doctor> doctors = appointmentService.getAllDoctors();
            return createDoctorsListResponse(doctors);
        } catch (Exception e) {
            return createErrorResponse("Failed to get doctors: " + e.getMessage());
        }
    }

    private String handleGetDoctorsBySpecialization(String data) {
        try {
            Map<String, String> params = parseParams(data);
            String specialization = params.get("specialization");

            List<Doctor> doctors = appointmentService.getDoctorsBySpecialization(specialization);
            return createDoctorsListResponse(doctors);
        } catch (Exception e) {
            return createErrorResponse("Failed to get doctors by specialization: " + e.getMessage());
        }
    }

    private String handleScheduleAppointment(String data) {
        try {
            Map<String, String> params = parseParams(data);
            Long patientId = Long.parseLong(params.get("patientId"));
            Long doctorId = Long.parseLong(params.get("doctorId"));
            LocalDateTime appointmentDate = LocalDateTime.parse(params.get("appointmentDate"));
            boolean hasReferral = Boolean.parseBoolean(params.get("hasReferral"));

            Appointment appointment = appointmentService.scheduleAppointment(patientId, doctorId, appointmentDate,
                    hasReferral);
            return createAppointmentResponse(appointment);
        } catch (Exception e) {
            return createErrorResponse("Failed to schedule appointment: " + e.getMessage());
        }
    }

    private String handleGetPatientAppointments(String data) {
        try {
            Map<String, String> params = parseParams(data);
            Long patientId = Long.parseLong(params.get("patientId"));

            List<Appointment> appointments = appointmentService.getPatientAppointments(patientId);
            return createAppointmentsListResponse(appointments);
        } catch (Exception e) {
            return createErrorResponse("Failed to get patient appointments: " + e.getMessage());
        }
    }

    private String handleGetDoctorAppointments(String data) {
        try {
            Map<String, String> params = parseParams(data);
            Long doctorId = Long.parseLong(params.get("doctorId"));

            List<Appointment> appointments = appointmentService.getDoctorAppointments(doctorId);
            return createAppointmentsListResponse(appointments);
        } catch (Exception e) {
            return createErrorResponse("Failed to get doctor appointments: " + e.getMessage());
        }
    }

    private String handleGetDoctorAppointmentsByDate(String data) {
        try {
            Map<String, String> params = parseParams(data);
            Long doctorId = Long.parseLong(params.get("doctorId"));
            LocalDate date = LocalDate.parse(params.get("date"));

            List<Appointment> appointments = appointmentService.getDoctorAppointmentsByDate(doctorId, date);
            return createAppointmentsListResponse(appointments);
        } catch (Exception e) {
            return createErrorResponse("Failed to get doctor appointments by date: " + e.getMessage());
        }
    }

    private String handleCancelAppointment(String data) {
        try {
            Map<String, String> params = parseParams(data);
            Long appointmentId = Long.parseLong(params.get("appointmentId"));
            Long userId = Long.parseLong(params.get("userId"));

            appointmentService.cancelAppointment(appointmentId, userId);
            return createSuccessResponse("Appointment cancelled successfully");
        } catch (Exception e) {
            return createErrorResponse("Failed to cancel appointment: " + e.getMessage());
        }
    }

    private String handleCompleteAppointment(String data) {
        try {
            Map<String, String> params = parseParams(data);
            Long appointmentId = Long.parseLong(params.get("appointmentId"));
            Long doctorId = Long.parseLong(params.get("doctorId"));

            appointmentService.completeAppointment(appointmentId, doctorId);
            return createSuccessResponse("Appointment completed successfully");
        } catch (Exception e) {
            return createErrorResponse("Failed to complete appointment: " + e.getMessage());
        }
    }

    private String handleCreateMedicalRecord(String data) {
        try {
            Map<String, String> params = parseParams(data);
            Long patientId = Long.parseLong(params.get("patientId"));
            Long doctorId = Long.parseLong(params.get("doctorId"));
            Long appointmentId = params.containsKey("appointmentId") ? Long.parseLong(params.get("appointmentId"))
                    : null;
            String diagnosis = params.get("diagnosis");
            String notes = params.get("notes");

            MedicalRecord record = medicalService.createMedicalRecord(patientId, doctorId, appointmentId, diagnosis,
                    notes);
            return createMedicalRecordResponse(record);
        } catch (Exception e) {
            return createErrorResponse("Failed to create medical record: " + e.getMessage());
        }
    }

    private String handleGetPatientMedicalHistory(String data) {
        try {
            Map<String, String> params = parseParams(data);
            Long patientId = Long.parseLong(params.get("patientId"));

            List<MedicalRecord> history = medicalService.getPatientMedicalHistory(patientId);
            return createMedicalRecordsListResponse(history);
        } catch (Exception e) {
            return createErrorResponse("Failed to get patient medical history: " + e.getMessage());
        }
    }

    private String handleCreatePrescription(String data) {
        try {
            Map<String, String> params = parseParams(data);

            // Check if medicalRecordId is provided directly
            if (params.containsKey("medicalRecordId")) {
                Long medicalRecordId = Long.parseLong(params.get("medicalRecordId"));
                String medication = params.get("medication");
                String dosage = params.get("dosage");
                String duration = params.get("duration");
                String instructions = params.get("instructions");

                Prescription prescription = medicalService.createPrescription(medicalRecordId, medication, dosage,
                        duration, instructions);
                return createPrescriptionResponse(prescription);
            }
            // Handle case where we need to create prescription for a patient directly
            else if (params.containsKey("patientId") && params.containsKey("doctorId")) {
                Long patientId = Long.parseLong(params.get("patientId"));
                Long doctorId = Long.parseLong(params.get("doctorId"));
                String medication = params.get("medication");
                String dosage = params.get("dosage");
                String duration = params.get("duration");
                String instructions = params.get("instructions");

                // Create a basic medical record for the prescription
                String diagnosis = "Prescription for " + medication;
                String notes = "Generated for prescription creation";

                MedicalRecord record = medicalService.createMedicalRecord(patientId, doctorId, null, diagnosis, notes);
                Prescription prescription = medicalService.createPrescription(record.getId(), medication, dosage,
                        duration, instructions);
                return createPrescriptionResponse(prescription);
            } else {
                throw new IllegalArgumentException(
                        "Either medicalRecordId or both patientId and doctorId must be provided");
            }
        } catch (Exception e) {
            return createErrorResponse("Failed to create prescription: " + e.getMessage());
        }
    }

    private String handleCreateTreatment(String data) {
        try {
            Map<String, String> params = parseParams(data);

            // Check if medicalRecordId is provided directly
            if (params.containsKey("medicalRecordId")) {
                Long medicalRecordId = Long.parseLong(params.get("medicalRecordId"));
                String treatmentName = params.get("treatmentName");
                String description = params.get("description");
                LocalDate startDate = params.containsKey("startDate") ? LocalDate.parse(params.get("startDate")) : null;
                LocalDate endDate = params.containsKey("endDate") ? LocalDate.parse(params.get("endDate")) : null;

                Treatment treatment = medicalService.createTreatment(medicalRecordId, treatmentName, description,
                        startDate, endDate);
                return createTreatmentResponse(treatment);
            }
            // Handle case where we need to create treatment for a patient directly
            else if (params.containsKey("patientId") && params.containsKey("doctorId")) {
                Long patientId = Long.parseLong(params.get("patientId"));
                Long doctorId = Long.parseLong(params.get("doctorId"));
                String treatmentName = params.get("treatmentName");
                String description = params.get("description");
                LocalDate startDate = params.containsKey("startDate") ? LocalDate.parse(params.get("startDate")) : null;
                LocalDate endDate = params.containsKey("endDate") ? LocalDate.parse(params.get("endDate")) : null;

                // Create a basic medical record for the treatment
                String diagnosis = "Treatment: " + treatmentName;
                String notes = "Generated for treatment creation";

                MedicalRecord record = medicalService.createMedicalRecord(patientId, doctorId, null, diagnosis, notes);
                Treatment treatment = medicalService.createTreatment(record.getId(), treatmentName, description,
                        startDate, endDate);
                return createTreatmentResponse(treatment);
            } else {
                throw new IllegalArgumentException(
                        "Either medicalRecordId or both patientId and doctorId must be provided");
            }
        } catch (Exception e) {
            return createErrorResponse("Failed to create treatment: " + e.getMessage());
        }
    }

    private String handleGetPatientPrescriptions(String data) {
        try {
            Map<String, String> params = parseParams(data);
            Long patientId = Long.parseLong(params.get("patientId"));

            List<Prescription> prescriptions = medicalService.getPatientPrescriptions(patientId);
            return createPrescriptionsListResponse(prescriptions);
        } catch (Exception e) {
            return createErrorResponse("Failed to get patient prescriptions: " + e.getMessage());
        }
    }

    private String handleGetPatientTreatments(String data) {
        try {
            Map<String, String> params = parseParams(data);
            Long patientId = Long.parseLong(params.get("patientId"));

            List<Treatment> treatments = medicalService.getPatientTreatments(patientId);
            return createTreatmentsListResponse(treatments);
        } catch (Exception e) {
            return createErrorResponse("Failed to get patient treatments: " + e.getMessage());
        }
    }

    private String handleCheckTimeSlot(String data) {
        try {
            Map<String, String> params = parseParams(data);
            Long doctorId = Long.parseLong(params.get("doctorId"));
            LocalDateTime appointmentDate = LocalDateTime.parse(params.get("appointmentDate"));

            boolean available = appointmentService.isTimeSlotAvailable(doctorId, appointmentDate);
            return createSuccessResponse("Time slot available: " + available);
        } catch (Exception e) {
            return createErrorResponse("Failed to check time slot: " + e.getMessage());
        }
    }

    private Map<String, String> parseParams(String data) {
        Map<String, String> params = new java.util.HashMap<>();
        if (data.trim().isEmpty())
            return params;

        String[] pairs = data.split("&");
        for (String pair : pairs) {
            String[] keyValue = pair.split("=", 2);
            if (keyValue.length == 2) {
                params.put(keyValue[0], keyValue[1]);
            }
        }
        return params;
    }

    // Response creation methods
    private String createSuccessResponse(String message) {
        return "SUCCESS" + DELIMITER + message;
    }

    private String createErrorResponse(String message) {
        return "ERROR" + DELIMITER + message;
    }

    private String createUserResponse(User user) {
        StringBuilder response = new StringBuilder("SUCCESS" + DELIMITER + "USER" + DELIMITER);
        response.append(user.getId()).append(DELIMITER);
        response.append(user.getEmail()).append(DELIMITER);
        response.append(user.getFirstName()).append(DELIMITER);
        response.append(user.getLastName()).append(DELIMITER);
        response.append(user.getUserType()).append(DELIMITER);
        response.append(user.getCreatedAt().format(DATETIME_FORMATTER)).append(DELIMITER);

        if (user instanceof Doctor doctor) {
            response.append("DOCTOR").append(DELIMITER);
            response.append(doctor.getSpecialization()).append(DELIMITER);
            response.append(doctor.isRequiresReferral());
        } else if (user instanceof Patient patient) {
            response.append("PATIENT").append(DELIMITER);
            response.append(patient.getDateOfBirth().format(DATE_FORMATTER)).append(DELIMITER);
            response.append(patient.getPhone()).append(DELIMITER);
            response.append(patient.getAddress()).append(DELIMITER);
            response.append(patient.getAllergies());
        }

        return response.toString();
    }

    private String createDoctorsListResponse(List<Doctor> doctors) {
        StringBuilder response = new StringBuilder("SUCCESS" + DELIMITER + "DOCTORS" + DELIMITER + doctors.size());
        for (Doctor doctor : doctors) {
            response.append(DELIMITER);
            response.append(doctor.getId()).append(",");
            response.append(doctor.getEmail()).append(",");
            response.append(doctor.getFirstName()).append(",");
            response.append(doctor.getLastName()).append(",");
            response.append(doctor.getSpecialization()).append(",");
            response.append(doctor.isRequiresReferral());
        }
        return response.toString();
    }

    private String createAppointmentResponse(Appointment appointment) {
        StringBuilder response = new StringBuilder("SUCCESS" + DELIMITER + "APPOINTMENT" + DELIMITER);
        response.append(appointment.getId()).append(DELIMITER);
        response.append(appointment.getPatient().getId()).append(DELIMITER);
        response.append(appointment.getDoctor().getId()).append(DELIMITER);
        response.append(appointment.getAppointmentDate().format(DATETIME_FORMATTER)).append(DELIMITER);
        response.append(appointment.getStatus()).append(DELIMITER);
        response.append(appointment.isHasReferral()).append(DELIMITER);
        response.append(appointment.getCreatedAt().format(DATETIME_FORMATTER));
        return response.toString();
    }

    private String createAppointmentsListResponse(List<Appointment> appointments) {
        StringBuilder response = new StringBuilder(
                "SUCCESS" + DELIMITER + "APPOINTMENTS" + DELIMITER + appointments.size());
        for (Appointment appointment : appointments) {
            response.append(DELIMITER);
            response.append(appointment.getId()).append(",");
            response.append(appointment.getPatient().getId()).append(",");
            response.append(appointment.getDoctor().getId()).append(",");
            response.append(appointment.getAppointmentDate().format(DATETIME_FORMATTER)).append(",");
            response.append(appointment.getStatus()).append(",");
            response.append(appointment.isHasReferral()).append(",");
            response.append(appointment.getCreatedAt().format(DATETIME_FORMATTER));
        }
        return response.toString();
    }

    private String createMedicalRecordResponse(MedicalRecord record) {
        StringBuilder response = new StringBuilder("SUCCESS" + DELIMITER + "MEDICAL_RECORD" + DELIMITER);
        response.append(record.getId()).append(DELIMITER);
        response.append(record.getPatient().getId()).append(DELIMITER);
        response.append(record.getDoctor().getId()).append(DELIMITER);
        response.append(record.getDiagnosis()).append(DELIMITER);
        response.append(record.getNotes()).append(DELIMITER);
        response.append(record.getCreatedAt().format(DATETIME_FORMATTER));
        return response.toString();
    }

    private String createMedicalRecordsListResponse(List<MedicalRecord> records) {
        StringBuilder response = new StringBuilder(
                "SUCCESS" + DELIMITER + "MEDICAL_RECORDS" + DELIMITER + records.size());
        for (MedicalRecord record : records) {
            response.append(DELIMITER);
            response.append(record.getId()).append(",");
            response.append(record.getPatient().getId()).append(",");
            response.append(record.getDoctor().getId()).append(",");
            response.append(record.getDiagnosis()).append(",");
            response.append(record.getNotes()).append(",");
            response.append(record.getCreatedAt().format(DATETIME_FORMATTER));
        }
        return response.toString();
    }

    private String createPrescriptionResponse(Prescription prescription) {
        StringBuilder response = new StringBuilder("SUCCESS" + DELIMITER + "PRESCRIPTION" + DELIMITER);
        response.append(prescription.getId()).append(DELIMITER);
        response.append(prescription.getMedicalRecord().getId()).append(DELIMITER);
        response.append(prescription.getMedication()).append(DELIMITER);
        response.append(prescription.getDosage()).append(DELIMITER);
        response.append(prescription.getDuration()).append(DELIMITER);
        response.append(prescription.getInstructions()).append(DELIMITER);
        response.append(prescription.getCreatedAt().format(DATETIME_FORMATTER));
        return response.toString();
    }

    private String createPrescriptionsListResponse(List<Prescription> prescriptions) {
        StringBuilder response = new StringBuilder(
                "SUCCESS" + DELIMITER + "PRESCRIPTIONS" + DELIMITER + prescriptions.size());
        for (Prescription prescription : prescriptions) {
            response.append(DELIMITER);
            response.append(prescription.getId()).append(",");
            response.append(prescription.getMedicalRecord().getId()).append(",");
            response.append(prescription.getMedication()).append(",");
            response.append(prescription.getDosage()).append(",");
            response.append(prescription.getDuration()).append(",");
            response.append(prescription.getInstructions()).append(",");
            response.append(prescription.getCreatedAt().format(DATETIME_FORMATTER));
        }
        return response.toString();
    }

    private String createTreatmentResponse(Treatment treatment) {
        StringBuilder response = new StringBuilder("SUCCESS" + DELIMITER + "TREATMENT" + DELIMITER);
        response.append(treatment.getId()).append(DELIMITER);
        response.append(treatment.getMedicalRecord().getId()).append(DELIMITER);
        response.append(treatment.getTreatmentName()).append(DELIMITER);
        response.append(treatment.getDescription()).append(DELIMITER);
        response.append(treatment.getStartDate() != null ? treatment.getStartDate().format(DATE_FORMATTER) : "")
                .append(DELIMITER);
        response.append(treatment.getEndDate() != null ? treatment.getEndDate().format(DATE_FORMATTER) : "")
                .append(DELIMITER);
        response.append(treatment.getCreatedAt().format(DATETIME_FORMATTER));
        return response.toString();
    }

    private String createTreatmentsListResponse(List<Treatment> treatments) {
        StringBuilder response = new StringBuilder(
                "SUCCESS" + DELIMITER + "TREATMENTS" + DELIMITER + treatments.size());
        for (Treatment treatment : treatments) {
            response.append(DELIMITER);
            response.append(treatment.getId()).append(",");
            response.append(treatment.getMedicalRecord().getId()).append(",");
            response.append(treatment.getTreatmentName()).append(",");
            response.append(treatment.getDescription()).append(",");
            response.append(treatment.getStartDate() != null ? treatment.getStartDate().format(DATE_FORMATTER) : "")
                    .append(",");
            response.append(treatment.getEndDate() != null ? treatment.getEndDate().format(DATE_FORMATTER) : "")
                    .append(",");
            response.append(treatment.getCreatedAt().format(DATETIME_FORMATTER));
        }
        return response.toString();
    }
}