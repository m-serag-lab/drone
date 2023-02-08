package com.musala.drone.model.drone;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

import com.musala.drone.model.medication.MedicationResponse;

@Getter
@Setter
public class DroneMedicationsResponse {
    private DroneResponse drone;
    private List<MedicationResponse> medications;
}
