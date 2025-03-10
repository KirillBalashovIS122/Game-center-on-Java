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
    }

    public enum Direction { UP, DOWN, LEFT, RIGHT }
}