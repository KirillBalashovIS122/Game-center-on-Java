package com.gamecenter.controller;

import com.gamecenter.model.GameResult;
import com.gamecenter.service.GameService;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/results")
public class GameResultController {
    private final GameService gameService;

    public GameResultController(GameService gameService) {
        this.gameService = gameService;
    }

    @GetMapping
    public List<GameResult> getAllResults() {
        return gameService.getLeaderboard(null);
    }

    @PostMapping
    public GameResult saveResult(@Valid @RequestBody GameResult result) {
        return gameService.saveResult(result);
    }
}