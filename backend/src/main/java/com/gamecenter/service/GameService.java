package com.gamecenter.service;

import com.gamecenter.model.GameResult;
import com.gamecenter.repository.GameResultRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class GameService {
    private static final Logger logger = LoggerFactory.getLogger(GameService.class);
    
    private final GameResultRepository gameResultRepository;

    public GameService(GameResultRepository gameResultRepository) {
        this.gameResultRepository = gameResultRepository;
    }

    @Transactional
    public GameResult saveResult(GameResult result) {
        if (result == null) {
            throw new IllegalArgumentException("Game result cannot be null");
        }

        validateGameResult(result);

        try {
            GameResult savedResult = gameResultRepository.save(result);
            logger.info("Saved game result: {}", savedResult);
            return savedResult;
        } catch (DataIntegrityViolationException e) {
            logger.error("Data integrity violation while saving result: {}", e.getMessage());
            throw new IllegalStateException("Could not save game result due to data validation", e);
        }
    }

    @Transactional
    public GameResult saveResult(String playerName, String gameType, int score) {
        GameResult result = new GameResult(playerName, gameType, score);
        return saveResult(result);
    }

    public List<GameResult> getLeaderboard(String gameType) {
        if (gameType == null || gameType.isBlank()) {
            return gameResultRepository.findAll(Sort.by(Sort.Direction.DESC, "score"));
        }
        return gameResultRepository.findByGameTypeOrderByScoreDesc(gameType);
    }

    public List<GameResult> getTopResults(String gameType, int limit) {
        if (limit <= 0) {
            throw new IllegalArgumentException("Limit must be positive");
        }
        
        Pageable pageable = PageRequest.of(0, limit, Sort.by(Sort.Direction.DESC, "score"));
        
        if (gameType == null || gameType.isBlank()) {
            return gameResultRepository.findTopResults(pageable);
        }
        
        return gameResultRepository.findTopResultsByGameType(gameType, pageable);
    }

    private void validateGameResult(GameResult result) {
        if (result.getPlayerName() == null || result.getPlayerName().isBlank()) {
            throw new IllegalArgumentException("Player name is required");
        }
        
        if (result.getGameType() == null || result.getGameType().isBlank()) {
            throw new IllegalArgumentException("Game type is required");
        }
        
        if (result.getScore() == null || result.getScore() < 0) {
            throw new IllegalArgumentException("Score must be positive");
        }
        
        if (result.getDate() == null) {
            throw new IllegalArgumentException("Date is required");
        }
    }

    @Transactional
    public void deleteResult(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Invalid result ID");
        }
        
        if (!gameResultRepository.existsById(id)) {
            throw new IllegalArgumentException("Result not found with ID: " + id);
        }
        
        gameResultRepository.deleteById(id);
        logger.info("Deleted game result with ID: {}", id);
    }
}