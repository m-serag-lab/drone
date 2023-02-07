package com.musala.drone.controller.drone;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import java.net.URI;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.musala.drone.model.drone.DroneRequest;
import com.musala.drone.service.drone.DroneService;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/drones")
@RequiredArgsConstructor
@Tag(name = "Drones")
public class DroneController {

    private final DroneService droneService;

    @GetMapping
    @Operation(summary = "list", description = "List all registered drones", tags = "Drones")
    public ResponseEntity<Object> list() {
        return ok(Map.of("items", droneService.list()));
    }

    @PostMapping
    @Operation(summary = "register", description = "Register new drone, drone registered with" +
            " `100 battery percentage` and in `IDLE state`", tags = "Drones")
    public ResponseEntity<Object> register(@RequestBody DroneRequest droneRequest) {
        return ResponseEntity.created(URI.create("/drones/" + droneService.register(droneRequest))).build();
    }
}
