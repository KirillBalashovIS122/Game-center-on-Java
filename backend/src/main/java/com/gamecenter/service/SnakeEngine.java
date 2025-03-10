package com.gamecenter.service;

import com.gamecenter.model.SnakeState;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class SnakeEngine {
    private final Map<String, SnakeState> games = new HashMap<>();

    public String createGame() {
        String gameId = UUID.randomUUID().toString();
        SnakeState state = new SnakeState();
        state.getSnake().add(new SnakeState.Point(5, 5));
        generateFood(state);
        games.put(gameId, state);
        return gameId;
    }

    public SnakeState handleAction(String gameId, String direction) {
        SnakeState state = games.get(gameId);
        if (state == null || state.isGameOver()) return null;

        state.setDirection(SnakeState.Direction.valueOf(direction));
        updateSnakePosition(state);
        return state;
    }

    public SnakeState getGameState(String gameId) {
        return games.get(gameId);
    }

    private void generateFood(SnakeState state) { /* Логика генерации еды */ }
    private void updateSnakePosition(SnakeState state) { /* Логика движения */ }
}