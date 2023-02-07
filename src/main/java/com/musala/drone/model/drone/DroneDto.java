package com.musala.drone.model.drone;

import lombok.Getter;
import lombok.Setter;

import com.fasterxml.jackson.annotation.JsonProperty;

@Getter
@Setter
public abstract class DroneDto {
    @JsonProperty("serial_number")
    private String serialNumber;
    private Model model;
    @JsonProperty("max_weight")
    private Double maxWeight;
}
