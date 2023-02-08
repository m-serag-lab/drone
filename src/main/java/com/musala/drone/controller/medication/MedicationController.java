package com.musala.drone.controller.medication;

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

import com.musala.drone.model.medication.MedicationRequest;
import com.musala.drone.model.medication.MedicationResponse;
import com.musala.drone.service.medication.MedicationService;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/medications")
@RequiredArgsConstructor
@Tag(name = "Medications")
public class MedicationController {

    private final MedicationService medicationService;

    @GetMapping
    @Operation(summary = "list", description = "List all medications", tags = "Medications")
    public ResponseEntity<Object> list() {
        return ok(Map.of("items", medicationService.list()));
    }

    @GetMapping("/{code}")
    @Operation(summary = "get", description = "Get medication by code", tags = "Medications")
    public ResponseEntity<MedicationResponse> get(@PathVariable("code") String code) {
        return ok(medicationService.get(code));
    }

    @PostMapping
    @Operation(summary = "create", description = "Create new medication", tags = "Medications",
            responses = {@ApiResponse(responseCode = "201",
                    description = "created")})
    public ResponseEntity<Object> create(@RequestBody MedicationRequest medicationRequest) {
        return ResponseEntity.created(URI.create("/medications/" + medicationService.create(medicationRequest))).build();
    }
}
