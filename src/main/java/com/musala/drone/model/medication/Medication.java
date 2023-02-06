package com.musala.drone.model.medication;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

import com.musala.drone.model.BaseEntity;

@Getter
@Setter
@Entity
public class Medication extends BaseEntity {
    @Column(name = "code")
    private String code;
    @Column(name = "name")
    private String name;
    @Column(name = "weight")
    private Double weight;
    @Column(name = "image_path")
    private String imagePath;
}
