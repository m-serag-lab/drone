package com.musala.drone.controller;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class DroneControllerIT {
    @Autowired
    private MockMvc mvc;

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
}
