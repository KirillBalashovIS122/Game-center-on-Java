package com.gamecenter.controller;

import com.gamecenter.model.SnakeState;
import com.gamecenter.service.SnakeEngine;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class SnakeControllerTest {

    private MockMvc mockMvc;

    @Mock
    private SnakeEngine snakeEngine;

    @InjectMocks
    private SnakeController snakeController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(snakeController).build();
    }

    @Test
    void createGame_ReturnsGameId() throws Exception {
        String testGameId = "test-id";
        when(snakeEngine.createGame()).thenReturn(testGameId);
        
        mockMvc.perform(post("/api/snake/new"))
               .andExpect(status().isOk())
               .andExpect(content().string(testGameId));
    }

    @Test
    void move_UpdatesGameState() throws Exception {
        SnakeState mockState = new SnakeState();
        when(snakeEngine.handleAction(anyString(), anyString()))
            .thenReturn(Optional.of(mockState));
        
        mockMvc.perform(post("/api/snake/move")
                .contentType("application/json")
                .content("{\"gameId\":\"test-id\",\"direction\":\"RIGHT\"}"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.gameOver").value(false));
    }
}