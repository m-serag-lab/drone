package com.musala.drone.controller.medication;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.musala.drone.model.medication.Medication;
import com.musala.drone.repository.medication.MedicationRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class MedicationControllerIT {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private MedicationRepository medicationRepository;

    @Test
    void test_get_thenValidateMapping() throws Exception {
        mvc.perform(get("/medications/M_001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code", Matchers.is("M_001")))
                .andExpect(jsonPath("$.name", Matchers.is("medication-name_001")))
                .andExpect(jsonPath("$.weight", Matchers.is(10.0)))
                .andExpect(jsonPath("$.image_path", Matchers.is("medication-image_001")));
    }

    @Test
    void test_listAll_thenValidateMapping() throws Exception {
        mvc.perform(get("/medications"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items", Matchers.hasSize(50)))
                .andExpect(jsonPath("$.items[0].code", Matchers.is("M_001")))
                .andExpect(jsonPath("$.items[1].code", Matchers.is("M_002")))
                .andExpect(jsonPath("$.items[2].code", Matchers.is("M_003")));
    }

    @Test
    void test_create_withValidRequest_thenValidateMapping() throws Exception {
        Medication medication = null;
        try {
            // Given: request body
            String expectedCode = "CODE-VALUE";
            String expectedName = "name_value";
            Double expectedWeight = 50.0;
            String expectedImagePath = "image.png";
            String request = String.format("{\"code\": \"%s\", \"name\": \"%s\"," +
                            " \"weight\": %s, \"image_path\": \"%s\"}",
                    expectedCode,
                    expectedName,
                    expectedWeight,
                    expectedImagePath);

            // When: register new drone
            MvcResult result = mvc.perform(post("/medications").contentType(MediaType.APPLICATION_JSON)
                            .content(request))
                    .andExpect(status().isCreated()).andReturn();

            // Then: validate saved drone
            String location = result.getResponse().getHeader("Location");
            Long id = Long.valueOf(location.substring(location.lastIndexOf("/") + 1));

            medication = medicationRepository.findById(id).get();
            assertEquals(expectedCode, medication.getCode());
            assertEquals(expectedName, medication.getName());
            assertEquals(expectedWeight, medication.getWeight());
            assertEquals(expectedImagePath, medication.getImagePath());
        } finally {
            // Cleanup
            safeDelete(medication);
        }
    }

    private void safeDelete(Medication medication) {
        if (medication != null) {
            medicationRepository.delete(medication);
        }
    }

}
