package com.musala.drone.controller.drone;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import java.net.URI;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.musala.drone.model.drone.DroneMedicationsRequest;
import com.musala.drone.model.drone.DroneMedicationsResponse;
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

    @GetMapping("/available")
    @Operation(summary = "list_available", description = "List available drones for loading", tags = "Drones")
    public ResponseEntity<Object> listAvailable() {
        return ok(Map.of("items", droneService.listAvailable()));
    }

    @GetMapping("/{serial_number}")
    @Operation(summary = "get", description = "check drone battery level for a given drone", tags = "Drones")
    public ResponseEntity<Object> get(@PathVariable("serial_number") String serialNumber) {
        return ok(droneService.get(serialNumber));
    }

    @PostMapping
    @Operation(summary = "register", description = "Register new drone, drone registered with" +
            " `100 battery percentage` and in `IDLE state`", tags = "Drones",
            responses = {@ApiResponse(responseCode = "201",
                    description = "created")})
    public ResponseEntity<Object> register(@RequestBody DroneRequest droneRequest) {
        return ResponseEntity.created(URI.create("/drones/" + droneService.register(droneRequest))).build();
    }

    @PostMapping("/{serial_number}/medications")
    @Operation(summary = "load_drone_with_medications", description = "loading a drone with medication items", tags = "Drones")
    public ResponseEntity<Object> loadMedications(@RequestBody DroneMedicationsRequest droneMedicationsRequest,
                                                  @PathVariable("serial_number") String serialNumber) {
        droneService.loadMedications(serialNumber, droneMedicationsRequest);
        return ok().build();
    }

    @GetMapping("/{serial_number}/medications")
    @Operation(summary = "list_drone_with_medications", description = "checking loaded medication" +
            " items for a given drone", tags = "Drones")
    public ResponseEntity<DroneMedicationsResponse> listMedications(@PathVariable("serial_number") String serialNumber) {
        return ok(droneService.listMedications(serialNumber));
    }

}
