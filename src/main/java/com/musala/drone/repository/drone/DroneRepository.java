package com.musala.drone.repository.drone;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.musala.drone.model.drone.Drone;

@Repository
public interface DroneRepository extends JpaRepository<Drone, Long> {
    @Query("select e from Drone e where e.serialNumber = :serialNumber")
    Optional<Drone> findBySerialNumber(@Param("serialNumber") String serialNumber);
}
