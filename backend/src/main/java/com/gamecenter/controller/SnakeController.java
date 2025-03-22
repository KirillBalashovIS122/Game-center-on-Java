package com.gamecenter.controller;

import com.gamecenter.model.SnakeState;
import com.gamecenter.service.SnakeEngine;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/snake")
public class SnakeController {
    private final SnakeEngine snakeEngine;

    public SnakeController(SnakeEngine snakeEngine) {
        this.snakeEngine = snakeEngine;
    }

    @PostMapping("/new")
    public String createGame() {
        return snakeEngine.createGame();
    }

    @PostMapping("/move")
    public SnakeState move(
        @RequestParam String gameId,
        @RequestParam String direction
    ) {
        return snakeEngine.handleAction(gameId, direction);
    }

    @GetMapping("/state")
    public SnakeState getState(@RequestParam String gameId) {
        return snakeEngine.getGameState(gameId);
    }
}