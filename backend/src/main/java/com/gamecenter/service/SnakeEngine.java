package com.gamecenter.service;

import com.gamecenter.model.SnakeState;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class SnakeEngine {
    private static final int BOARD_WIDTH = 20;
    private static final int BOARD_HEIGHT = 20;
    private final Map<String, SnakeState> games = new HashMap<>();

    public SnakeState getGameState(String gameId) {
        return games.get(gameId);
    }

    public String createGame() {
        String gameId = UUID.randomUUID().toString();
        SnakeState state = new SnakeState();
        
        state.getSnake().add(new SnakeState.Point(10, 10));
        state.setDirection(SnakeState.Direction.RIGHT);
        generateFood(state);
        
        games.put(gameId, state);
        return gameId;
    }

    public SnakeState handleAction(String gameId, String direction) {
        SnakeState state = games.get(gameId);
        if (state == null || state.isGameOver()) return state;

        state.setDirection(SnakeState.Direction.valueOf(direction));
        moveSnake(state);
        return state;
    }

    private void moveSnake(SnakeState state) {
        SnakeState.Point head = state.getSnake().get(0);
        SnakeState.Point newHead = new SnakeState.Point(
            head.getX() + state.getDirection().dx,
            head.getY() + state.getDirection().dy
        );

        if (isCollision(newHead, state)) {
            state.setGameOver(true);
            return;
        }

        state.getSnake().add(0, newHead);

        if (newHead.getX() == state.getFood().getX() && 
            newHead.getY() == state.getFood().getY()) {
            state.setScore(state.getScore() + 10);
            generateFood(state);
        } else {
            state.getSnake().remove(state.getSnake().size() - 1);
        }
    }

    private boolean isCollision(SnakeState.Point point, SnakeState state) {
        return point.getX() < 0 || point.getX() >= BOARD_WIDTH || 
               point.getY() < 0 || point.getY() >= BOARD_HEIGHT ||
               state.getSnake().stream().anyMatch(p -> 
                   p.getX() == point.getX() && p.getY() == point.getY()
               );
    }

    private void generateFood(SnakeState state) {
        Random rand = new Random();
        SnakeState.Point food;
        boolean isOnSnake;
        do {
            int x = rand.nextInt(BOARD_WIDTH);
            int y = rand.nextInt(BOARD_HEIGHT);
            food = new SnakeState.Point(x, y);
            isOnSnake = state.getSnake().stream().anyMatch(p -> 
                p.getX() == x && p.getY() == y
            );
        } while (isOnSnake);
        state.setFood(food);
    }
}
