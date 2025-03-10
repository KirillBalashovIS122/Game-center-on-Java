package com.gamecenter.model;

import lombok.Data;

@Data
public class TetrisState {
    private int[][] board = new int[20][10];
    private int currentPiece;
    private int nextPiece;
    private int score;
    private boolean gameOver;

    public void moveLeft() { /* Логика движения влево */ }
    public void moveRight() { /* Логика движения вправо */ }
    public void rotate() { /* Логика поворота фигуры */ }
    public void drop() { /* Логика падения фигуры */ }
}