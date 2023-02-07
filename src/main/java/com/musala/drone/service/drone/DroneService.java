package com.musala.drone.service.drone;

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.musala.drone.exception.InvalidDroneException;
import com.musala.drone.model.drone.Drone;
import com.musala.drone.model.drone.DroneRequest;
import com.musala.drone.model.drone.DroneResponse;
import com.musala.drone.repository.drone.DroneRepository;

import static com.musala.drone.model.drone.State.IDLE;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Service
@RequiredArgsConstructor
public class DroneService {
    private static final int MAX_SERIAL_NUMBER = 100;

    private final DroneRepository droneRepository;
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
        if (isNotBlank(request.getSerialNumber()) && request.getSerialNumber().length() > MAX_SERIAL_NUMBER) {
            final String errorMsg = String.format("invalid serial_number length '%s'", request.getSerialNumber());
            throw new InvalidDroneException(request, errorMsg);
        }
    }
}
