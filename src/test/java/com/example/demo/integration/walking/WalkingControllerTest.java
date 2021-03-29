package com.example.demo.integration.walking;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.demo.util.TestFactory;
import com.example.demo.common.LoadLoggedUser;
import com.example.demo.walking.domain.entity.*;
import com.example.demo.walking.domain.valueobject.CEP;
import com.example.demo.walking.domain.valueobject.Email;
import com.example.demo.walking.domain.valueobject.Telephone;
import com.example.demo.walking.infra.repository.CaregiverRepository;
import com.example.demo.walking.infra.repository.OwnerRepository;
import com.example.demo.walking.infra.repository.PetRepository;
import com.example.demo.walking.infra.repository.WalkingRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;

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
    @Autowired private CaregiverRepository caregiverRepository;
    @Autowired private WalkingRepository walkingRepository;
    @Autowired private ObjectMapper mapper;
    @Autowired private LoadLoggedUser loadLoggedUser;

    @BeforeAll
    public void setup() {
        var owner =
                Owner.of(
                        new CEP("12345678"),
                        new Email("teste@teste.com"),
                        "Felipe",
                        "Abda Street",
                        478);
        var caregiver = Caregiver.of(new Telephone("12345678910"), new Email("a@t2.com"), "Marcia");
        this.caregiverRepository.save(caregiver);
        this.ownerRepository.save(owner);
        this.petRepository.save(Pet.of("Tex", "-", "-", owner));
    }

    @DisplayName("Should create an walking")
    @Test
    public void testCreateValidWalking() throws Exception {
        var createWalkRequest =
                TestFactory.createWalkRequest(30, "-", "-", Collections.singletonList(1L));
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

    @DisplayName(
            "Create walking with one, or more pets, that were not found in database should throw an exception")
    @Test
    public void testNotFoundPets() throws Exception {
        var createWalkRequest = TestFactory.createWalkRequest(60, "-", "-", Arrays.asList(1L, 2L));
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

    @DisplayName("Should accept walk and change status to ACCEPTED")
    @Test
    public void testAcceptWalk() throws Exception {
        var pets = this.petRepository.findAll();
        var walking =
                TestFactory.createWalking(LocalDateTime.now().plusDays(1L), "-", "-", 30, pets);
        var walkingInsert = this.walkingRepository.save(walking);
        this.mockMvc.perform(get("/api/v1/walk/accept/" + walkingInsert.getId())).andExpect(status().isOk());
        var walkingRow = this.walkingRepository.findById(walkingInsert.getId()).get();
        assertEquals(WalkingStatus.ACCEPTED, walkingRow.getStatus());
    }

    @DisplayName(
            "Try to accept already accepted or canceled walk should return bad request and not change status")
    @Test
    public void testTryToAcceptAlreadyAcceptedOrCanceledWalk() throws Exception {
        var pets = this.petRepository.findAll();
        var walking =
                TestFactory.createWalking(LocalDateTime.now().plusDays(1L), "-", "-", 30, pets);
        var walkingInsert = this.walkingRepository.save(walking);
        walkingInsert.acceptWalk(this.loadLoggedUser.loadLoggedUser());
        this.walkingRepository.save(walkingInsert);
        var response =
                this.mockMvc
                        .perform(get("/api/v1/walk/accept/" + walkingInsert.getId()))
                        .andExpect(status().isBadRequest())
                        .andReturn();
        var json = new JSONObject(response.getResponse().getContentAsString());
        assertEquals("This walk was already accepted or canceled", json.getString("message"));
        assertFalse(json.getBoolean("success"));
        var walkingRow = this.walkingRepository.findById(walkingInsert.getId()).get();
        assertEquals(WalkingStatus.ACCEPTED, walkingRow.getStatus());
    }

    @DisplayName("Test start walking")
    @Test
    public void testStartWalking() throws Exception {
        var pets = this.petRepository.findAll();
        var walking =
                TestFactory.createWalking(LocalDateTime.now().plusDays(1L), "-", "-", 30, pets);
        var walkingInsert = this.walkingRepository.save(walking);
        walkingInsert.acceptWalk(this.loadLoggedUser.loadLoggedUser());
        this.walkingRepository.save(walkingInsert);
        this.mockMvc.perform(get("/api/v1/walk/start/" + walkingInsert.getId())).andExpect(status().isOk());
        var walkingRow = this.walkingRepository.findById(walkingInsert.getId()).get();
        assertNotNull(walkingRow.getStartDate());
        assertNull(walkingRow.getFinishDate());
    }

    @DisplayName(
            "Try to start walking with null caregiver or nonexistent walking should return an response error and not change walking atributes")
    @Test
    public void testStartWalkingWithoutCaregiverOrNonexistentWalking() throws Exception {
        var responseNonexistentWalking =
                this.mockMvc
                        .perform(get("/api/v1/walk/start/0"))
                        .andExpect(status().isBadRequest())
                        .andReturn();
        var jsonNonexistentWalking =
                new JSONObject(responseNonexistentWalking.getResponse().getContentAsString());
        assertEquals(
                "Walking not found, was not possible to start the walking",
                jsonNonexistentWalking.getString("message"));
        assertFalse(jsonNonexistentWalking.getBoolean("success"));

        var pets = this.petRepository.findAll();
        var walking =
                TestFactory.createWalking(LocalDateTime.now().plusDays(1L), "-", "-", 30, pets);
        var walkingInsert = this.walkingRepository.save(walking);
        var responseNullCaregiver =
                this.mockMvc
                        .perform(get("/api/v1/walk/start/" + walkingInsert.getId()))
                        .andExpect(status().isUnauthorized())
                        .andReturn();
        var jsonNullCaregiver =
                new JSONObject(responseNullCaregiver.getResponse().getContentAsString());
        assertEquals(
                "You are not the responsible for this walking",
                jsonNullCaregiver.getString("message"));
        assertFalse(jsonNullCaregiver.getBoolean("success"));
        var walkingRow = this.walkingRepository.findById(walkingInsert.getId()).get();
        assertNull(walkingRow.getStartDate());
        assertNull(walkingRow.getFinishDate());
    }

    @DisplayName("Test finish walking")
    @Test
    public void testFinishWalking() throws Exception {
        var pets = this.petRepository.findAll();
        var walking =
                TestFactory.createWalking(LocalDateTime.now().plusDays(1L), "-", "-", 30, pets);
        var walkingInsert = this.walkingRepository.save(walking);
        walkingInsert.acceptWalk(this.loadLoggedUser.loadLoggedUser());
        walkingInsert.startWalk();
        this.walkingRepository.save(walkingInsert);
        this.mockMvc.perform(get("/api/v1/walk/finish/" + walkingInsert.getId())).andExpect(status().isOk());
        var walkingRow = this.walkingRepository.findById(walkingInsert.getId()).get();
        assertNotNull(walkingRow.getStartDate());
        assertNotNull(walkingRow.getFinishDate());
    }
}
