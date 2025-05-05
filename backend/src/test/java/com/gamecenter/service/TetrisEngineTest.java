package com.gamecenter.service;

import com.gamecenter.model.TetrisState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TetrisEngineTest {

    private TetrisEngine tetrisEngine;

    @BeforeEach
    void setUp() {
        tetrisEngine = new TetrisEngine();
    }

    @Test
    void createGame_ReturnsValidGameId() {
        String gameId = tetrisEngine.createGame();

        assertNotNull(gameId, "Game ID should not be null");
        assertFalse(gameId.isEmpty(), "Game ID should not be empty");

        TetrisState state = tetrisEngine.getGameState(gameId);
        assertNotNull(state, "Game state should exist for the created game ID");
        assertFalse(state.isGameOver(), "New game should not be over");
        assertEquals(0, state.getScore(), "Initial score should be 0");
        assertNotNull(state.getCurrentPiece(), "Current piece should be initialized");
    }

    @Test
    void handleAction_MoveRight_UpdatesPiecePosition() {
        String gameId = tetrisEngine.createGame();
        TetrisState initialState = tetrisEngine.getGameState(gameId);
        assertNotNull(initialState, "Initial game state should exist");

        int initialPieceX = initialState.getPieceX();

        TetrisState updatedState = tetrisEngine.handleAction(gameId, "MOVE_RIGHT");

        assertNotNull(updatedState, "Updated state should not be null");
        assertFalse(updatedState.isGameOver(), "Game should not be over after moving right");
        assertEquals(initialPieceX + 1, updatedState.getPieceX(), "Piece should move one position to the right");
        assertEquals(initialState.getPieceY(), updatedState.getPieceY(), "Piece Y position should not change");
        assertEquals(initialState.getScore(), updatedState.getScore(), "Score should not change after moving right");
    }
}