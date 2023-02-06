package com.musala.drone.repository.medication;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.musala.drone.model.medication.Medication;

@Repository
public interface MedicationRepository extends JpaRepository<Medication, Long> {
}
