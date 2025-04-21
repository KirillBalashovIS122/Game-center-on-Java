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

        Optional<SnakeState> stateOpt = snakeEngine.getGameState(gameId);
        assertTrue(stateOpt.isPresent());
        SnakeState state = stateOpt.get();
        assertFalse(state.isGameOver());
        assertEquals(3, state.getSnake().size());
        assertNotNull(state.getFood());
        assertEquals(SnakeState.Direction.RIGHT, state.getDirection());
    }

    @Test
    void handleAction_ChangesDirection() {
        String gameId = snakeEngine.createGame();

        Optional<SnakeState> resultDown = snakeEngine.handleAction(gameId, "DOWN");
        assertTrue(resultDown.isPresent());
        assertEquals(SnakeState.Direction.DOWN, resultDown.get().getDirection());

        gameId = snakeEngine.createGame();
        Optional<SnakeState> resultUp = snakeEngine.handleAction(gameId, "UP");
        assertTrue(resultUp.isPresent());
        assertEquals(SnakeState.Direction.UP, resultUp.get().getDirection());

        gameId = snakeEngine.createGame();
        Optional<SnakeState> resultRight = snakeEngine.handleAction(gameId, "RIGHT");
        assertTrue(resultRight.isPresent());
        assertEquals(SnakeState.Direction.RIGHT, resultRight.get().getDirection());
    }

    @Test
    void handleAction_RejectsOppositeDirection() {
        String gameIdRight = snakeEngine.createGame();
        Optional<SnakeState> resultRightLeft = snakeEngine.handleAction(gameIdRight, "LEFT");
        assertTrue(resultRightLeft.isPresent());
        assertEquals(SnakeState.Direction.RIGHT, resultRightLeft.get().getDirection());

        String gameIdLeft = snakeEngine.createGame();
        snakeEngine.handleAction(gameIdLeft, "DOWN");
        Optional<SnakeState> resultDownUp = snakeEngine.handleAction(gameIdLeft, "UP");
        assertTrue(resultDownUp.isPresent());
        snakeEngine.handleAction(gameIdLeft, "LEFT");
        Optional<SnakeState> resultLeftRight = snakeEngine.handleAction(gameIdLeft, "RIGHT");
        assertTrue(resultLeftRight.isPresent());
        assertEquals(SnakeState.Direction.LEFT, resultLeftRight.get().getDirection());

        String gameIdUp = snakeEngine.createGame();
        snakeEngine.handleAction(gameIdUp, "UP");
        Optional<SnakeState> resultUpDown = snakeEngine.handleAction(gameIdUp, "DOWN");
        assertTrue(resultUpDown.isPresent());
        assertEquals(SnakeState.Direction.UP, resultUpDown.get().getDirection());

        String gameIdDown = snakeEngine.createGame();
        snakeEngine.handleAction(gameIdDown, "DOWN");
        Optional<SnakeState> resultDownUp2 = snakeEngine.handleAction(gameIdDown, "UP");
        assertTrue(resultDownUp2.isPresent());
        assertEquals(SnakeState.Direction.DOWN, resultDownUp2.get().getDirection());
    }

    @Test
    void handleAction_DoesNotChangeDirectionAfterGameOver() {
        String gameId = snakeEngine.createGame();
        SnakeState state = snakeEngine.getGameState(gameId).orElseThrow();
        state.setGameOver(true);

        Optional<SnakeState> result = snakeEngine.handleAction(gameId, "DOWN");
        assertFalse(result.isPresent());
    }

    @Test
    void moveSnake_ChangesPosition() {
        String gameId = snakeEngine.createGame();
        SnakeState initialState = snakeEngine.getGameState(gameId).orElseThrow();
        SnakeState.Point initialHead = initialState.getSnake().get(0);

        SnakeState newState = snakeEngine.moveSnake(initialState);
        SnakeState.Point newHead = newState.getSnake().get(0);

        assertNotEquals(initialHead, newHead);
        assertEquals(initialState.getSnake().size(), newState.getSnake().size());
    }

    @Test
    void eatFood_IncreasesSnakeLength() {
        String gameId = snakeEngine.createGame();
        SnakeState state = snakeEngine.getGameState(gameId).orElseThrow();

        SnakeState.Point food = state.getFood();
        state.getSnake().set(0, new SnakeState.Point(food.getX() - 1, food.getY()));
        state.setDirection(SnakeState.Direction.RIGHT);

        int initialLength = state.getSnake().size();
        SnakeState newState = snakeEngine.moveSnake(state);

        assertEquals(initialLength + 1, newState.getSnake().size());
        assertNotEquals(food, newState.getFood());
    }
}