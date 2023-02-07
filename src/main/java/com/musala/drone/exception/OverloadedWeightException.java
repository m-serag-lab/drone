package com.musala.drone.exception;

import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class OverloadedWeightException extends RuntimeException {

    private final String serialNumber;
    private final double maxWeight;
    private final double currentWeight;
    private final List<String> medicationCodes;

    public OverloadedWeightException(String serialNumber,
                                     double maxWeight, double currentWeight,
                                     List<String> medicationCodes) {
        super(String.format("drone '%s' cannot be loaded by medications '%s'" +
                        " since drone's max weight is '%s' and medications weight is '%s'",
                serialNumber,
                maxWeight,
                currentWeight,
                medicationCodes.stream().collect(Collectors.joining(", "))));
        this.serialNumber = serialNumber;
        this.maxWeight = maxWeight;
        this.currentWeight = currentWeight;
        this.medicationCodes = medicationCodes;
    }
}
