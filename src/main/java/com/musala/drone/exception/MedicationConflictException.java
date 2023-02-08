package com.musala.drone.exception;

import lombok.Getter;

@Getter
public class MedicationConflictException extends RuntimeException {

    private final String code;

    public MedicationConflictException(String code) {
        super("medication with code: " + code + " already exists");
        this.code = code;
    }
}
