package com.musala.drone.controller.drone;

import lombok.RequiredArgsConstructor;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.musala.drone.service.drone.DroneService;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/drones")
@RequiredArgsConstructor
public class DroneController {

    private final DroneService droneService;

    @GetMapping
    public ResponseEntity<Object> list() {
        return ok(Map.of("items", droneService.list()));
    }

}
