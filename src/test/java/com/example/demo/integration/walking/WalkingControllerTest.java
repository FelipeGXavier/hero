package com.example.demo.integration.walking;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.demo.walking.adapters.CreateWalkRequest;
import com.example.demo.walking.domain.entity.Owner;
import com.example.demo.walking.domain.entity.Pet;
import com.example.demo.walking.domain.entity.Walking;
import com.example.demo.walking.domain.entity.WalkingStatus;
import com.example.demo.walking.domain.valueobject.CEP;
import com.example.demo.walking.domain.valueobject.Email;
import com.example.demo.walking.infra.repository.OwnerRepository;
import com.example.demo.walking.infra.repository.PetRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;

import org.json.JSONObject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class WalkingControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private PetRepository petRepository;
    @Autowired private OwnerRepository ownerRepository;
    @Autowired private ObjectMapper mapper;

    @BeforeAll
    public void setup() {
        var owner =
                Owner.of(
                        new CEP("12345678"),
                        new Email("teste@teste.com"),
                        "Felipe",
                        "Abda Street",
                        478);
        ownerRepository.save(owner);
        petRepository.save(Pet.of("Tex", "-", "-", owner));
    }

    @Test
    @DisplayName("Should create an walking")
    public void testCreateValidWalking() throws Exception {
        var createWalkRequest = this.createRequest(30, "-", "-", Collections.singletonList(1L));
        var response =
                this.mockMvc
                        .perform(
                                post("/api/v1/walk")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .accept(MediaType.APPLICATION_JSON)
                                        .content(this.mapper.writeValueAsString(createWalkRequest)))
                        .andExpect(status().isCreated())
                        .andReturn();
        var jsonResponse = new JSONObject(response.getResponse().getContentAsString());
        var insertedDate = LocalDateTime.parse(jsonResponse.getString("scheduledDate"));
        assertEquals(30, jsonResponse.getInt("duration"));
        assertEquals(25.0, jsonResponse.getDouble("price"));
        assertEquals(1, jsonResponse.getJSONArray("pets").length());
        assertEquals("PENDING", jsonResponse.getString("status"));
        assertEquals(createWalkRequest.getScheduledDate(), insertedDate);
        assertEquals("null", jsonResponse.get("caregiver").toString());
        assertEquals("null", jsonResponse.get("startDate").toString());
        assertEquals("null", jsonResponse.get("finishDate").toString());
    }

    @Test
    @DisplayName(
            "Create walking with one, or more pets, that were not found in database should throw an exception")
    public void testNotFoundPets() throws Exception {
        var createWalkRequest = this.createRequest(60, "-", "-", Arrays.asList(1L, 2L));
        var response =
                this.mockMvc
                        .perform(
                                post("/api/v1/walk")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .accept(MediaType.APPLICATION_JSON)
                                        .content(this.mapper.writeValueAsString(createWalkRequest)))
                        .andExpect(status().isBadRequest())
                        .andReturn();
        var jsonResponse = new JSONObject(response.getResponse().getContentAsString());
        assertFalse(jsonResponse.getBoolean("success"));
        assertEquals("One or more pets were not found", jsonResponse.getString("message"));
    }

    private CreateWalkRequest createRequest(
            int duration, String latitude, String longitude, List<Long> pets) {
        var scheduledDate = LocalDateTime.now().plusDays(3).truncatedTo(ChronoUnit.SECONDS);
        return new CreateWalkRequest(scheduledDate, duration, latitude, longitude, pets);
    }
}
