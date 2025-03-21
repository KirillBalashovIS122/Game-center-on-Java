package com.gamecenter.repository;

import com.gamecenter.model.GameResult;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface GameResultRepository extends JpaRepository<GameResult, Long> {
    List<GameResult> findByGameType(String gameType);
    List<GameResult> findByGameTypeOrderByScoreDesc(String gameType);
    List<GameResult> findAllByOrderByScoreDesc();
}