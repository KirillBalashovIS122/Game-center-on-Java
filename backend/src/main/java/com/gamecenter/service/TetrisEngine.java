package com.gamecenter.service;

import com.gamecenter.model.TetrisState;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class TetrisEngine {
    private final Map<String, TetrisState> games = new HashMap<>();

    public String createGame() {
        String gameId = UUID.randomUUID().toString();
        games.put(gameId, new TetrisState());
        return gameId;
    }

    public TetrisState handleAction(String gameId, String action) {
        TetrisState state = games.get(gameId);
        if (state == null || state.isGameOver()) {
            return state;
        }

        switch (action) {
            case "MOVE_LEFT":
                state.moveLeft();
                break;
            case "MOVE_RIGHT":
                state.moveRight();
                break;
            case "ROTATE":
                state.rotate();
                break;
            case "SOFT_DROP":
                state.softDrop();
                break;
            case "HARD_DROP":
                state.drop();
                break;
        }

        state.update();
        return state;
    }

    public TetrisState getGameState(String gameId) {
        return games.get(gameId);
    }
}