package com.musala.drone.service.drone;

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.musala.drone.model.drone.DroneResponse;
import com.musala.drone.repository.drone.DroneRepository;

@Service
@RequiredArgsConstructor
public class DroneService {
    private final DroneRepository droneRepository;
    private final ModelMapper mapper;

    public List<DroneResponse> list() {
        return droneRepository
                .findAll()
                .stream()
                .map(drone -> mapper.map(drone, DroneResponse.class))
                .toList();
    }
}
