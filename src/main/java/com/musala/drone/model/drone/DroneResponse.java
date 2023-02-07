package com.musala.drone.model.drone;

import lombok.Getter;
import lombok.Setter;

import com.fasterxml.jackson.annotation.JsonProperty;

@Getter
@Setter
public class DroneResponse extends DroneDto {
    @JsonProperty("id")
    private Long id;
    @JsonProperty("battery_percentage")
    private Double batteryPercentage;
    private State state;
}
