package com.musala.drone.exception;

import lombok.Getter;

@Getter
public class DroneConflictException extends RuntimeException {

    private final String serialNumber;

    public DroneConflictException(String serialNumber) {
        super("drone with serial_number: " + serialNumber + " already exists");
        this.serialNumber = serialNumber;
    }
}
