package com.musala.drone.exception;

import lombok.Getter;

@Getter
public class LoadedDroneException extends RuntimeException {
    private final String serialNumber;

    public LoadedDroneException(String serialNumber) {
        super(String.format("drone '%s' is already loaded", serialNumber));
        this.serialNumber = serialNumber;
    }
}
