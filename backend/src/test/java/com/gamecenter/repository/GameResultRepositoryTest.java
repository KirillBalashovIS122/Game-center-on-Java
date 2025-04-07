package com.gamecenter.repository;

import com.gamecenter.model.GameResult;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class GameResultRepositoryTest {

    @Autowired
    private GameResultRepository gameResultRepository;

    @Test
    @Transactional
    void findByGameTypeOrderByScoreDesc_ReturnsFilteredResults() {
        GameResult snakeResult = new GameResult();
        snakeResult.setGameType("SNAKE");
        snakeResult.setPlayerName("Test Player");
        snakeResult.setScore(100);
        snakeResult.setDate(LocalDateTime.now());
        
        GameResult tetrisResult = new GameResult();
        tetrisResult.setGameType("TETRIS");
        tetrisResult.setPlayerName("Test Player");
        tetrisResult.setScore(200);
        tetrisResult.setDate(LocalDateTime.now());
        
        gameResultRepository.save(snakeResult);
        gameResultRepository.save(tetrisResult);

        List<GameResult> snakeResults = gameResultRepository.findByGameTypeOrderByScoreDesc("SNAKE");

        assertEquals(1, snakeResults.size());
        assertEquals("SNAKE", snakeResults.get(0).getGameType());
        assertEquals(100, snakeResults.get(0).getScore());
    }
}