package com.gamecenter.model;

import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Data
public class SnakeState {
    private List<Point> snake = new ArrayList<>();
    private Point food;
    private Direction direction;
    private int score;
    private boolean gameOver;

    @Data
    public static class Point {
        private int x;
        private int y;

        public Point() {}
        
        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int getX() { return x; }
        public int getY() { return y; }
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