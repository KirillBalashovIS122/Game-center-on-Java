package com.gamecenter.repository;

import com.gamecenter.model.GameResult;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface GameResultRepository extends JpaRepository<GameResult, Long> {

    List<GameResult> findByGameTypeOrderByScoreDesc(String gameType);

    @Query("SELECT gr FROM GameResult gr WHERE gr.gameType = :gameType ORDER BY gr.score DESC")
    List<GameResult> findTopResultsByGameType(@Param("gameType") String gameType, Pageable pageable);

    @Query("SELECT gr FROM GameResult gr ORDER BY gr.score DESC")
    List<GameResult> findTopResults(Pageable pageable);

    Page<GameResult> findByDateBetween(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);

    List<GameResult> findByPlayerNameOrderByDateDesc(String playerName);

    long countByGameType(String gameType);

    @Query("SELECT MAX(gr.score) FROM GameResult gr WHERE gr.gameType = :gameType")
    Integer findMaxScoreByGameType(@Param("gameType") String gameType);

    @Query("SELECT gr FROM GameResult gr WHERE gr.playerName = :playerName AND gr.gameType = :gameType ORDER BY gr.score DESC")
    List<GameResult> findByPlayerAndGameType(@Param("playerName") String playerName, @Param("gameType") String gameType);

    @Query("SELECT gr FROM GameResult gr WHERE gr.score >= :minScore AND gr.score <= :maxScore ORDER BY gr.score DESC")
    List<GameResult> findByScoreBetween(@Param("minScore") int minScore, @Param("maxScore") int maxScore);

    @Query("SELECT DISTINCT gr.gameType FROM GameResult gr")
    List<String> findAllGameTypes();

    @Query("SELECT gr FROM GameResult gr WHERE gr.date >= :date ORDER BY gr.score DESC")
    List<GameResult> findRecentResults(@Param("date") LocalDateTime date);

    @Query("SELECT gr.playerName, MAX(gr.score) FROM GameResult gr WHERE gr.gameType = :gameType GROUP BY gr.playerName ORDER BY MAX(gr.score) DESC")
    List<Object[]> findLeaderboardByGameType(@Param("gameType") String gameType);
}