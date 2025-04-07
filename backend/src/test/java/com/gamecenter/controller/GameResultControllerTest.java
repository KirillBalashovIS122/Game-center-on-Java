package com.gamecenter.controller;

import com.gamecenter.service.GameService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(GameResultController.class)
@ActiveProfiles("test")
class GameResultControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GameService gameService;

    @Test
    void getLeaderboard_ReturnsResults() throws Exception {
        when(gameService.getLeaderboard("SNAKE")).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/results?gameType=SNAKE"))
               .andExpect(status().isOk());
    }
}