package com.gamecenter.controller;

import com.gamecenter.model.TetrisState;
import com.gamecenter.service.TetrisEngine;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class TetrisControllerTest {

    private MockMvc mockMvc;

    @Mock
    private TetrisEngine tetrisEngine;

    @InjectMocks
    private TetrisController tetrisController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(tetrisController).build();
    }

    @Test
    void createGame_ReturnsGameId() throws Exception {
        String testGameId = "tetris-test-id";
        when(tetrisEngine.createGame()).thenReturn(testGameId);

        mockMvc.perform(post("/api/tetris/new"))
               .andExpect(status().isOk())
               .andExpect(content().string(testGameId));
    }

    @Test
    void action_UpdatesGameState() throws Exception {
        TetrisState mockState = new TetrisState();
        mockState.setPieceX(5);
        mockState.setGameOver(false);

        when(tetrisEngine.handleAction(anyString(), anyString())).thenReturn(mockState);

        mockMvc.perform(post("/api/tetris/action")
                .param("gameId", "tetris-test-id")
                .param("action", "MOVE_RIGHT"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.gameOver").value(false))
               .andExpect(jsonPath("$.pieceX").value(5));
    }
}