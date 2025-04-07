package com.gamecenter.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity
@Table(name = "game_result")
public class GameResult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Player name cannot be empty")
    @Size(min = 1, max = 50, message = "Player name must be between 1 and 50 characters")
    @Column(nullable = false, length = 50)
    private String playerName;

    @NotBlank(message = "Game type cannot be empty")
    @Size(min = 1, max = 20, message = "Game type must be between 1 and 20 characters")
    @Column(nullable = false, length = 20)
    private String gameType;

    @NotNull(message = "Score cannot be null")
    @Column(nullable = false)
    private Integer score;

    @Column(nullable = false, updatable = false)
    private LocalDateTime date;

    public GameResult() {
        this.date = LocalDateTime.now();
    }

    public GameResult(String playerName, String gameType, Integer score) {
        this();
        this.playerName = playerName;
        this.gameType = gameType;
        this.score = score;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public String getGameType() {
        return gameType;
    }

    public void setGameType(String gameType) {
        this.gameType = gameType;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        if (score < 0) {
            throw new IllegalArgumentException("Score cannot be negative");
        }
        this.score = score;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        if (date == null) {
            throw new IllegalArgumentException("Date cannot be null");
        }
        this.date = date;
    }

    @Override
    public String toString() {
        return "GameResult{" +
                "id=" + id +
                ", playerName='" + playerName + '\'' +
                ", gameType='" + gameType + '\'' +
                ", score=" + score +
                ", date=" + date +
                '}';
    }
}