package com.gamecenter.model;

import jakarta.persistence.*;
import lombok.Data;
import jakarta.validation.constraints.NotBlank; // Исправленный импорт
import jakarta.validation.constraints.NotNull;  // Исправленный импорт
import java.time.LocalDateTime;

@Data
@Entity
public class GameResult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Имя игрока обязательно")
    private String playerName;

    @NotBlank(message = "Тип игры обязателен")
    private String gameType;

    @NotNull(message = "Счёт обязателен")
    private int score;

    private LocalDateTime date = LocalDateTime.now();
}