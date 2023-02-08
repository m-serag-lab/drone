package com.musala.drone.service.drone;

import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Set;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.musala.drone.exception.InvalidDroneException;
import com.musala.drone.exception.InvalidMedicationException;
import com.musala.drone.exception.LoadedDroneException;
import com.musala.drone.exception.LowBatteryException;
import com.musala.drone.exception.OverloadedWeightException;
import com.musala.drone.model.drone.Drone;
import com.musala.drone.model.drone.DroneMedicationsRequest;
import com.musala.drone.model.drone.DroneMedicationsResponse;
import com.musala.drone.model.drone.DroneRequest;
import com.musala.drone.model.drone.DroneResponse;
import com.musala.drone.model.medication.Medication;
import com.musala.drone.model.medication.MedicationResponse;
import com.musala.drone.repository.drone.DroneRepository;
import com.musala.drone.repository.medication.MedicationRepository;

import static com.musala.drone.model.drone.State.IDLE;
import static com.musala.drone.model.drone.State.LOADED;
import static java.util.stream.Collectors.toSet;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Service
@RequiredArgsConstructor
public class DroneService {
    private static final int MAX_SERIAL_NUMBER = 100;
    private static final int MAX_WEIGHT = 500;
    public static final double MIN_BATTERY_PERCENTAGE = 25.0;

    private final DroneRepository droneRepository;
    private final MedicationRepository medicationRepository;
    private final ModelMapper mapper;

    public List<DroneResponse> list() {
        return droneRepository
                .findAll()
                .stream()
                .map(drone -> mapper.map(drone, DroneResponse.class))
                .toList();
    }

    public Long register(DroneRequest request) {
        validate(request);
        Drone drone = new Drone();
        drone.setState(IDLE);
        drone.setBatteryPercentage(100.0);
        mapper.map(request, drone);

        return droneRepository.save(drone).getId();
    }

    private void validate(DroneRequest request) {
        //TODO: validate unique serial number
        if (request.getMaxWeight() != null && request.getMaxWeight() > MAX_WEIGHT) {
            final String errorMsg = String.format("invalid max_weight '%s', max value is '%s'",
                    request.getMaxWeight(), MAX_WEIGHT);
            throw new InvalidDroneException(request, errorMsg);
        } else if (isNotBlank(request.getSerialNumber()) && request.getSerialNumber().length() > MAX_SERIAL_NUMBER) {
            final String errorMsg = String.format("invalid serial_number length '%s'", request.getSerialNumber());
            throw new InvalidDroneException(request, errorMsg);
        }
    }

    @Transactional
    public void loadMedications(String serialNumber, DroneMedicationsRequest droneMedicationsRequest) {
        Drone drone = droneRepository.findBySerialNumber(serialNumber)
                .orElseThrow(() -> new InvalidDroneException(serialNumber));
        validateStatus(drone);

        List<Medication> medications = medicationRepository
                .findAllByCodes(droneMedicationsRequest.getMedicationCodes());
        validateMedicationCodes(medications, droneMedicationsRequest);
        validateMedicationWeights(drone, medications);

        drone.setState(LOADED);
        drone.setMedications(medications);
        droneRepository.save(drone);
    }

    private void validateMedicationWeights(Drone drone, List<Medication> medications) {
        double medicationsWeightSum = medications.stream().mapToDouble(Medication::getWeight).sum();
        if (medicationsWeightSum > drone.getMaxWeight()) {
            throw new OverloadedWeightException(drone.getSerialNumber(),
                    drone.getMaxWeight(),
                    medicationsWeightSum,
                    medications.stream().map(Medication::getCode).toList());
        }
    }

    private void validateMedicationCodes(List<Medication> medications, DroneMedicationsRequest droneMedicationsRequest) {
        if (medications.size() != droneMedicationsRequest.getMedicationCodes().size()) {
            // Validates that all medication codes persisted in DB
            List<String> requestedCodes = droneMedicationsRequest.getMedicationCodes();
            Set<String> foundedCodes = medications.stream().map(Medication::getCode).collect(toSet());
            List<String> notFoundCodes = requestedCodes.stream()
                    .filter(code -> !foundedCodes.contains(code))
                    .toList();
            throw new InvalidMedicationException(notFoundCodes);
        }
    }

    private void validateStatus(Drone drone) {
        if (drone.getState() == LOADED) {
            throw new LoadedDroneException(drone.getSerialNumber());
        } else if (drone.getBatteryPercentage() < MIN_BATTERY_PERCENTAGE) {
            throw new LowBatteryException(drone.getSerialNumber(), drone.getBatteryPercentage());
        }
    }

    @Transactional
    public DroneMedicationsResponse listMedications(String serialNumber) {
        Drone drone = droneRepository.findBySerialNumber(serialNumber)
                .orElseThrow(() -> new InvalidDroneException(serialNumber));

        List<MedicationResponse> medicationResponses = drone.getMedications()
                .stream()
                .map(medication -> mapper.map(medication, MedicationResponse.class))
                .toList();
        DroneMedicationsResponse response = new DroneMedicationsResponse();
        response.setDrone(mapper.map(drone, DroneResponse.class));
        response.setMedications(medicationResponses);
        return response;
    }

    public DroneResponse get(String serialNumber) {
        Drone drone = droneRepository.findBySerialNumber(serialNumber)
                .orElseThrow(() -> new InvalidDroneException(serialNumber));

        return mapper.map(drone, DroneResponse.class);
    }

    public List<DroneResponse> listAvailable() {
        return droneRepository
                .findAllByStateAndBatteryPercentage(IDLE, MIN_BATTERY_PERCENTAGE)
                .stream()
                .map(drone -> mapper.map(drone, DroneResponse.class))
                .toList();
    }
}
