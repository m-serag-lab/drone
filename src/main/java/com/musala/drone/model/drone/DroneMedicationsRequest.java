package com.musala.drone.model.drone;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

@Getter
@Setter
public class DroneMedicationsRequest {
    @JsonProperty("medication_codes")
    private List<String> medicationCodes;
}
