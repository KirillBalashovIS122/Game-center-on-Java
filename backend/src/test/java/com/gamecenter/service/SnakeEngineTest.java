package com.gamecenter.service;

import com.gamecenter.model.SnakeState;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class SnakeEngineTest {

    @Autowired
    private SnakeEngine snakeEngine;

    @Test
    void createGame_ReturnsValidGameId() {
        String gameId = snakeEngine.createGame();
        assertNotNull(gameId);
        assertFalse(gameId.isEmpty());
        
        Optional<SnakeState> state = snakeEngine.getGameState(gameId);
        assertTrue(state.isPresent());
        assertFalse(state.get().isGameOver());
    }

    @Test
    void handleAction_ChangesDirection() {
        String gameId = snakeEngine.createGame();
        SnakeState initialState = snakeEngine.getGameState(gameId).orElseThrow();
        
        Optional<SnakeState> result = snakeEngine.handleAction(gameId, "DOWN");
        
        assertTrue(result.isPresent());
        assertEquals(SnakeState.Direction.DOWN, result.get().getDirection());
        assertNotEquals(initialState.getDirection(), result.get().getDirection());
    }

    @Test
    void handleAction_RejectsOppositeDirection() {
        String gameId = snakeEngine.createGame();
        snakeEngine.handleAction(gameId, "RIGHT");
        
        Optional<SnakeState> result = snakeEngine.handleAction(gameId, "LEFT");
        
        assertTrue(result.isPresent());
        assertEquals(SnakeState.Direction.RIGHT, result.get().getDirection());
    }
}