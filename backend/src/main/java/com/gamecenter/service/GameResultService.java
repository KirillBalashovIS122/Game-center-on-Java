package com.gamecenter.service;

import com.gamecenter.model.GameResult;
import com.gamecenter.repository.GameResultRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GameResultService {
    private final GameResultRepository gameResultRepository;

    public List<GameResult> getResultsByGame(String gameName) {
        return gameResultRepository.findByGameName(gameName);
    }

    public void saveResult(GameResult result) {
        gameResultRepository.save(result);
    }
}
