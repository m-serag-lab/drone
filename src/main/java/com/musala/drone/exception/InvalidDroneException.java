package com.musala.drone.exception;

import lombok.Getter;

import com.musala.drone.model.drone.DroneRequest;

@Getter
public class InvalidDroneException extends RuntimeException {

    private final DroneRequest droneRequest;

    public InvalidDroneException(DroneRequest droneRequest, String message) {
        super(message);
        this.droneRequest = droneRequest;
    }

    public InvalidDroneException(String serialNumber) {
        super(String.format("serial_number: '%s' is not found", serialNumber));
        droneRequest = null;
    }
}
