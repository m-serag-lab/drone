package com.musala.drone.repository.medication;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.musala.drone.model.medication.Medication;

@Repository
public interface MedicationRepository extends JpaRepository<Medication, Long> {
    @Query("select e from Medication e where e.code in :codes")
    List<Medication> findAllByCodes(@Param("codes") List<String> codes);
}
