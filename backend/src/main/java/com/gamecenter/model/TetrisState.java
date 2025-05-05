package com.gamecenter.model;

import lombok.Data;
import java.util.Arrays;
import java.util.Random;

@Data
public class TetrisState {
    private static final int WIDTH = 10;
    private static final int HEIGHT = 20;
    private static final int[][][] SHAPES = {
        {{1, 1, 1, 1}},                // I-фигура
        {{1, 1}, {1, 1}},              // O-фигура
        {{0, 1, 0}, {1, 1, 1}},        // T-фигура
        {{1, 0}, {1, 0}, {1, 1}},      // L-фигура
        {{0, 1}, {0, 1}, {1, 1}},      // J-фигура
        {{0, 1, 1}, {1, 1, 0}},        // Z-фигура
        {{1, 1, 0}, {0, 1, 1}}         // S-фигура
    };
    
    private int[][] board = new int[HEIGHT][WIDTH];
    private int[][] currentPiece;
    private int pieceX;
    private int pieceY;
    private int nextPiece;
    private int score;
    private boolean gameOver;
    private boolean needsNewPiece = false;
    private Random random = new Random();

    public TetrisState() {
        nextPiece = random.nextInt(SHAPES.length);
        spawnNewPiece();
    }

    private void spawnNewPiece() {
        currentPiece = SHAPES[nextPiece];
        nextPiece = random.nextInt(SHAPES.length);
        pieceX = WIDTH / 2 - currentPiece[0].length / 2;
        pieceY = 0;
        needsNewPiece = false;
        
        if (checkCollision(pieceX, pieceY, currentPiece)) {
            gameOver = true;
        }
    }

    public void rotate() {
        if (gameOver || needsNewPiece || currentPiece.length == currentPiece[0].length) {
            return;
        }

        int[][] rotated = new int[currentPiece[0].length][currentPiece.length];
        
        for (int i = 0; i < currentPiece.length; i++) {
            for (int j = 0; j < currentPiece[i].length; j++) {
                rotated[j][currentPiece.length - 1 - i] = currentPiece[i][j];
            }
        }

        int[][] tests = {
            {0, 0},
            {1, 0},
            {-1, 0},
            {0, -1},
            {2, 0},
            {-2, 0}
        };

        for (int[] test : tests) {
            int newX = pieceX + test[0];
            int newY = pieceY + test[1];
            
            if (!checkCollision(newX, newY, rotated)) {
                currentPiece = rotated;
                pieceX = newX;
                pieceY = newY;
                return;
            }
        }
    }

    private boolean checkCollision(int x, int y, int[][] piece) {
        for (int i = 0; i < piece.length; i++) {
            for (int j = 0; j < piece[i].length; j++) {
                if (piece[i][j] != 0) {
                    int newX = x + j;
                    int newY = y + i;
                    
                    if (newX < 0 || newX >= WIDTH || newY >= HEIGHT) {
                        return true;
                    }
                    if (newY >= 0 && board[newY][newX] != 0) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private void mergePiece() {
        for (int i = 0; i < currentPiece.length; i++) {
            for (int j = 0; j < currentPiece[i].length; j++) {
                if (currentPiece[i][j] != 0) {
                    int y = pieceY + i;
                    if (y >= 0) board[y][pieceX + j] = 1;
                }
            }
        }
        clearLines();
        needsNewPiece = true;
    }

    private void clearLines() {
        int linesCleared = 0;
        for (int i = HEIGHT - 1; i >= 0; i--) {
            if (Arrays.stream(board[i]).allMatch(cell -> cell != 0)) {
                System.arraycopy(board, 0, board, 1, i);
                Arrays.fill(board[0], 0);
                linesCleared++;
                i++;
            }
        }
        
        if (linesCleared > 0) {
            score += linesCleared * 100;
        }
    }

    public boolean update() {
        if (gameOver) return false;
        
        if (needsNewPiece) {
            spawnNewPiece();
            return true;
        }
        
        if (!checkCollision(pieceX, pieceY + 1, currentPiece)) {
            pieceY++;
            return true;
        } else {
            mergePiece();
            return false;
        }
    }

    public void moveLeft() {
        if (!gameOver && !needsNewPiece && !checkCollision(pieceX - 1, pieceY, currentPiece)) {
            pieceX--;
        }
    }

    public void moveRight() {
        if (!gameOver && !needsNewPiece && !checkCollision(pieceX + 1, pieceY, currentPiece)) {
            pieceX++;
        }
    }

    public void drop() {
        if (gameOver) return;
        
        while (!checkCollision(pieceX, pieceY + 1, currentPiece)) {
            pieceY++;
        }
        mergePiece();
    }

    public void softDrop() {
        if (!gameOver && !needsNewPiece && !checkCollision(pieceX, pieceY + 1, currentPiece)) {
            pieceY++;
        }
    }
}