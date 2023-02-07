package com.musala.drone.exception;

import lombok.Getter;

import java.util.List;

import static java.util.stream.Collectors.joining;

@Getter
public class InvalidMedicationException extends RuntimeException {
    private final List<String> notFoundCodes;

    public InvalidMedicationException(List<String> notFoundCodes) {
        super(String.format("codes: '%s' are not found", notFoundCodes.stream().collect(joining(", "))));
        this.notFoundCodes = notFoundCodes;
    }
}
