package com.example.demo.integration;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class PingTest {

    @Autowired private MockMvc mockMvc;

    @Test
    @DisplayName("Test ping request")
    public void testPingRequest() throws Exception {
        var response =
                this.mockMvc
                        .perform(get("/api/ping"))
                        .andDo(print())
                        .andExpect(status().isOk())
                        .andReturn();
        assertEquals("Pong", response.getResponse().getContentAsString());
    }
}
