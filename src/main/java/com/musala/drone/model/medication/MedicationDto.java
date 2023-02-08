package com.musala.drone.model.medication;

import lombok.Getter;
import lombok.Setter;

import com.fasterxml.jackson.annotation.JsonProperty;

@Getter
@Setter
public abstract class MedicationDto {
    private String code;
    private String name;
    private Double weight;
    @JsonProperty("image_path")
    private String imagePath;
}
