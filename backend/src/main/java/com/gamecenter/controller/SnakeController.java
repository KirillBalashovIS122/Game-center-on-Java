package com.gamecenter.controller;

import com.gamecenter.model.SnakeState;
import com.gamecenter.service.SnakeEngine;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/snake")
public class SnakeController {
    private final SnakeEngine engine;

    public SnakeController(SnakeEngine engine) {
        this.engine = engine;
    }

    @PostMapping("/new")
    public String createGame() {
        return engine.createGame();
    }

    @PostMapping("/move")
    public SnakeState move(
        @RequestParam String gameId,
        @RequestParam String direction
    ) {
        return engine.handleAction(gameId, direction);
    }

    @GetMapping("/state")
    public SnakeState getState(@RequestParam String gameId) {
        return engine.getGameState(gameId);
    }
}