package com.musala.drone.model.drone;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

import com.musala.drone.model.BaseEntity;
import com.musala.drone.model.medication.Medication;

@Getter
@Setter
@Entity
public class Drone extends BaseEntity {
    @Column(name = "serial_number")
    private String serialNumber;
    @Column(name = "model")
    @Enumerated(EnumType.STRING)
    private Model model;
    @Column(name = "max_weight")
    private Double maxWeight;
    @Column(name = "battery_percentage")
    private Double batteryPercentage;
    @Column(name = "state")
    @Enumerated(EnumType.STRING)
    private State state;
    @ManyToMany
    @JoinTable(
            name = "drone_medication",
            joinColumns = {@JoinColumn(name = "drone_id")},
            inverseJoinColumns = {@JoinColumn(name = "medication_id")}
    )
    private List<Medication> medications = new ArrayList<>();
}
