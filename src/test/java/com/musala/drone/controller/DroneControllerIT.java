package com.musala.drone.controller;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.musala.drone.model.drone.Drone;
import com.musala.drone.model.medication.Medication;
import com.musala.drone.repository.drone.DroneRepository;
import com.musala.drone.repository.medication.MedicationRepository;

import static com.musala.drone.model.drone.State.IDLE;
import static com.musala.drone.model.drone.State.LOADED;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class DroneControllerIT {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private DroneRepository droneRepository;
    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private MedicationRepository medicationRepository;

    @Test
    void test_listAll_thenValidateMapping() throws Exception {
        mvc.perform(get("/drones"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items", Matchers.hasSize(10)))
                .andExpect(jsonPath("$.items[0].serial_number", Matchers.is("1001")))
                .andExpect(jsonPath("$.items[1].serial_number", Matchers.is("1002")))
                .andExpect(jsonPath("$.items[2].serial_number", Matchers.is("1003")))
                .andExpect(jsonPath("$.items[3].serial_number", Matchers.is("1004")))
                .andExpect(jsonPath("$.items[4].serial_number", Matchers.is("1005")))
                .andExpect(jsonPath("$.items[5].serial_number", Matchers.is("1006")))
                .andExpect(jsonPath("$.items[6].serial_number", Matchers.is("1007")))
                .andExpect(jsonPath("$.items[7].serial_number", Matchers.is("1008")))
                .andExpect(jsonPath("$.items[8].serial_number", Matchers.is("1009")))
                .andExpect(jsonPath("$.items[9].serial_number", Matchers.is("1010")));
    }

    @Test
    void test_register_withValidRequest_thenValidateMapping() throws Exception {
        Drone drone = null;
        try {
            // Given: request body
            String expectedSerialNumber = "serial_number_value";
            Double expectedMaxWeight = 500.0;
            String expectedModel = "HEAVY";
            String request = String.format("{\"serial_number\": \"%s\", \"max_weight\": %s, \"model\": \"%s\"}",
                    expectedSerialNumber,
                    expectedMaxWeight,
                    expectedModel);

            // When: register new drone
            MvcResult result = mvc.perform(post("/drones").contentType(MediaType.APPLICATION_JSON)
                            .content(request))
                    .andExpect(status().isCreated()).andReturn();

            // Then: validate saved drone
            String location = result.getResponse().getHeader("Location");
            Long id = Long.valueOf(location.substring(location.lastIndexOf("/") + 1));

            drone = droneRepository.findById(id).get();
            assertEquals(expectedSerialNumber, drone.getSerialNumber());
            assertEquals(expectedMaxWeight, drone.getMaxWeight());
            assertEquals(expectedModel, drone.getModel().name());
            assertEquals(IDLE, drone.getState());
            assertEquals(100.0, drone.getBatteryPercentage());
            assertNotNull(drone.getMedications());

        } finally {
            // Cleanup
            safeDelete(drone);
        }
    }

    @Test
    void test_loadMedications_withValidRequest_thenValidateDroneMedications() throws Exception {
        Drone drone = null;
        try {
            // Given: drone, medication 1, medication 2 and request body
            String serialNumber = "serial_number_value";
            drone = new Drone();
            drone.setSerialNumber(serialNumber);
            drone.setState(IDLE);
            drone.setBatteryPercentage(80.0);
            drone.setMaxWeight(35.0);
            droneRepository.save(drone);

            Medication medication1 = new Medication();
            medication1.setCode("CODE-1");
            medication1.setWeight(10.0);
            medicationRepository.save(medication1);

            Medication medication2 = new Medication();
            medication2.setCode("CODE-2");
            medication2.setWeight(10.0);
            medicationRepository.save(medication2);

            String request = String.format("{\"medication_codes\": [\"%s\", \"%s\"]}",
                    "CODE-1",
                    "CODE-2");

            // When: register new drone
            String uri = "/drones/" + serialNumber + "/medications";
            mvc.perform(post(uri).contentType(MediaType.APPLICATION_JSON)
                            .content(request))
                    .andExpect(status().isOk());

            // Then: validate saved drone
            String findDroneWithMedicationsJPQL = String.format("select e from Drone e" +
                    " join fetch e.medications m" +
                    " where e.serialNumber = '%s'", serialNumber);
            drone = (Drone) entityManager.createQuery(findDroneWithMedicationsJPQL).getResultList().get(0);

            assertEquals(serialNumber, drone.getSerialNumber());
            assertEquals(LOADED, drone.getState());
            assertNotNull(drone.getMedications());
            assertEquals("CODE-1", drone.getMedications().get(0).getCode());
            assertEquals("CODE-2", drone.getMedications().get(1).getCode());

        } finally {
            // Cleanup: delete medication and drone
            safeDelete(drone);
        }
    }

    private void safeDelete(Drone drone) {
        if (drone != null) {
            droneRepository.delete(drone);
        }
    }
}
