package com.musala.drone.exception;

import lombok.Getter;

import static com.musala.drone.service.drone.DroneService.MIN_BATTERY_PERCENTAGE;

@Getter
public class LowBatteryException extends RuntimeException {
    private final String serialNumber;
    private final double batteryPercentage;

    public LowBatteryException(String serialNumber, double batteryPercentage) {
        super(String.format("drone '%s' battery's percentage is '%s' is lower than the minimum percentage '%s'",
                serialNumber, batteryPercentage, MIN_BATTERY_PERCENTAGE));
        this.serialNumber = serialNumber;
        this.batteryPercentage = batteryPercentage;
    }
}
