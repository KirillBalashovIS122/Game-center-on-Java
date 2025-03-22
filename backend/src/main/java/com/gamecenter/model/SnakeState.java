package com.gamecenter.model;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class SnakeState {
    private List<Point> snake = new ArrayList<>();
    private Point food;
    private Direction direction;
    private int score;
    private boolean gameOver;
    private String playerName;
    private LocalDateTime lastActivity;

    @Data
    public static class Point {
        private final int x;
        private final int y;
    }

    public enum Direction {
        UP(0, -1),
        DOWN(0, 1),
        LEFT(-1, 0),
        RIGHT(1, 0);

        public final int dx;
        public final int dy;

        Direction(int dx, int dy) {
            this.dx = dx;
            this.dy = dy;
        }
    }
}