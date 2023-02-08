package com.musala.drone.service.medication;

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.musala.drone.exception.InvalidMedicationException;
import com.musala.drone.exception.MedicationConflictException;
import com.musala.drone.model.medication.Medication;
import com.musala.drone.model.medication.MedicationRequest;
import com.musala.drone.model.medication.MedicationResponse;
import com.musala.drone.repository.medication.MedicationRepository;

@Service
@RequiredArgsConstructor
public class MedicationService {
    private final MedicationRepository medicationRepository;
    private final ModelMapper mapper;

    public List<MedicationResponse> list() {
        return medicationRepository.findAll().stream()
                .map(medication -> mapper.map(medication, MedicationResponse.class))
                .toList();
    }

    public MedicationResponse get(String code) {
        return medicationRepository.findByCode(code)
                .map(medication -> mapper.map(medication, MedicationResponse.class))
                .orElseThrow();
    }

    public Long create(MedicationRequest medicationRequest) {
        validate(medicationRequest);
        return medicationRepository.save(mapper.map(medicationRequest, Medication.class)).getId();
    }

    private void validateCodeFormat(String code) {
        for (char c : code.toCharArray()) {
            if (!Character.isUpperCase(c) && !Character.isDigit(c) && c != '_') {
                throw new InvalidMedicationException(code);
            }
        }
    }

    private void validateNameFormat(String name) {
        for (char c : name.toCharArray()) {
            if (!Character.isLetterOrDigit(c) && c != '_' && c != '-') {
                throw new InvalidMedicationException(name);
            }
        }
    }

    private void validate(MedicationRequest medicationRequest) {
        if (medicationRepository.findByCode(medicationRequest.getCode()).isPresent()) {
            throw new MedicationConflictException(medicationRequest.getCode());
        }
        validateCodeFormat(medicationRequest.getCode());
        validateNameFormat(medicationRequest.getName());
    }
}
