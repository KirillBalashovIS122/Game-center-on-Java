package com.gamecenter.controller;

import com.gamecenter.dto.SnakeMoveRequest;
import com.gamecenter.exception.GameNotFoundException;
import com.gamecenter.model.SnakeState;
import com.gamecenter.service.SnakeEngine;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/snake")
public class SnakeController {
    private final SnakeEngine snakeEngine;

    public SnakeController(SnakeEngine snakeEngine) {
        this.snakeEngine = snakeEngine;
    }

    @PostMapping("/new")
    @ResponseStatus(HttpStatus.CREATED)
    public String createGame(
        @RequestParam(required = false, defaultValue = "Player") String playerName
    ) {
        return snakeEngine.createGame(playerName);
    }

    @PostMapping("/move")
    public SnakeState move(@Valid @RequestBody SnakeMoveRequest request) {
        return snakeEngine.handleAction(request.gameId(), request.direction());
    }

    @GetMapping("/state")
    public SnakeState getState(@RequestParam String gameId) {
        return snakeEngine.getGameState(gameId)
            .orElseThrow(() -> new GameNotFoundException("Game not found"));
    }
}