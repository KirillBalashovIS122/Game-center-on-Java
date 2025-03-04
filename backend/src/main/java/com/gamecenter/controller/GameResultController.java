package com.gamecenter.controller;

import com.gamecenter.model.GameResult;
import com.gamecenter.service.GameResultService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/results")
@RequiredArgsConstructor
public class GameResultController {
    private final GameResultService gameResultService;

    @GetMapping("/{gameName}")
    public List<GameResult> getResults(@PathVariable String gameName) {
        return gameResultService.getResultsByGame(gameName);
    }

    @PostMapping
    public void saveResult(@RequestBody GameResult result) {
        gameResultService.saveResult(result);
    }
}
