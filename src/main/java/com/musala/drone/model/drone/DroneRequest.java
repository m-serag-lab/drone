package com.musala.drone.model.drone;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DroneRequest extends DroneDto {
    public DroneRequest(String serialNumber, Double maxWeight, Model model) {
        super();
        this.setSerialNumber(serialNumber);
        this.setMaxWeight(maxWeight);
        this.setModel(model);
    }
}
