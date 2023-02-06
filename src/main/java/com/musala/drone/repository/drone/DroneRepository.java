package com.musala.drone.repository.drone;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.musala.drone.model.drone.Drone;

@Repository
public interface DroneRepository extends JpaRepository<Drone, Long> {
}
