package com.gamecenter.service;

import com.gamecenter.model.GameResult;
import com.gamecenter.repository.GameResultRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class GameService {
    private final GameResultRepository repository;

    public GameService(GameResultRepository repository) {
        this.repository = repository;
    }

    public GameResult saveResult(GameResult result) {
        return repository.save(result);
    }

    public List<GameResult> getLeaderboard(String gameType) {
        return gameType == null 
            ? repository.findAllByOrderByScoreDesc()
            : repository.findByGameTypeOrderByScoreDesc(gameType);
    }
}