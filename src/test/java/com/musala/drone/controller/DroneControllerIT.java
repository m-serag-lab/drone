package com.musala.drone.controller;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.musala.drone.model.drone.Drone;
import com.musala.drone.repository.drone.DroneRepository;

import static com.musala.drone.model.drone.State.IDLE;
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
                            .content(request.toString()))
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
            if (drone != null) {
                droneRepository.delete(drone);
            }
        }
    }
}
