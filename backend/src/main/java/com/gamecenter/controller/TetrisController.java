package com.gamecenter.controller;

import com.gamecenter.model.TetrisState;
import com.gamecenter.service.TetrisEngine;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tetris")
public class TetrisController {
    private final TetrisEngine engine;

    public TetrisController(TetrisEngine engine) {
        this.engine = engine;
    }

    @PostMapping("/new")
    public String createGame() {
        return engine.createGame();
    }

    @PostMapping("/action")
    public TetrisState handleAction(
        @RequestParam String gameId,
        @RequestParam String action
    ) {
        return engine.handleAction(gameId, action);
    }

    @GetMapping("/state")
    public TetrisState getState(@RequestParam String gameId) {
        return engine.getGameState(gameId);
    }
}