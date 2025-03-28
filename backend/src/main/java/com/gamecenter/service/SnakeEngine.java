package com.gamecenter.service;

import com.gamecenter.model.GameResult;
import com.gamecenter.model.SnakeState;
import com.gamecenter.repository.GameResultRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class SnakeEngine {
    private static final int BOARD_WIDTH = 20;
    private static final int BOARD_HEIGHT = 20;
    private final Map<String, SnakeState> games = new HashMap<>();
    private final GameResultRepository gameResultRepository;

    public String createGame(String playerName) {
        String gameId = UUID.randomUUID().toString();
        SnakeState state = new SnakeState();
        state.setPlayerName(playerName);
        state.setSnake(new ArrayList<>(List.of(new SnakeState.Point(10, 10))));
        state.setDirection(SnakeState.Direction.RIGHT);
        state.setLastActivity(LocalDateTime.now());
        generateFood(state);
        games.put(gameId, state);
        return gameId;
    }

    public SnakeState handleAction(String gameId, String direction) {
        SnakeState state = games.get(gameId);
        if (state == null || state.isGameOver()) return state;

        try {
            SnakeState.Direction newDirection = SnakeState.Direction.valueOf(direction);
            if (isOppositeDirection(state.getDirection(), newDirection)) return state;
            state.setDirection(newDirection);
            moveSnake(state);
            
            if (state.isGameOver()) {
                saveGameResult(state);
            }
            
            state.setLastActivity(LocalDateTime.now());
            return state;
        } catch (IllegalArgumentException e) {
            return state;
        }
    }

    public Optional<SnakeState> getGameState(String gameId) {
        return Optional.ofNullable(games.get(gameId));
    }

    @Scheduled(fixedRate = 60_000)
    public void cleanupOldGames() {
        games.entrySet().removeIf(entry -> 
            entry.getValue().isGameOver() && 
            entry.getValue().getLastActivity().isBefore(LocalDateTime.now().minusHours(1))
        );
    }

    private boolean isOppositeDirection(SnakeState.Direction current, SnakeState.Direction newDir) {
        return (current == SnakeState.Direction.UP && newDir == SnakeState.Direction.DOWN) ||
               (current == SnakeState.Direction.DOWN && newDir == SnakeState.Direction.UP) ||
               (current == SnakeState.Direction.LEFT && newDir == SnakeState.Direction.RIGHT) ||
               (current == SnakeState.Direction.RIGHT && newDir == SnakeState.Direction.LEFT);
    }

    private void moveSnake(SnakeState state) {
        SnakeState.Point head = state.getSnake().get(0);
        int newX = head.getX() + state.getDirection().dx;
        int newY = head.getY() + state.getDirection().dy;
        SnakeState.Point newHead = new SnakeState.Point(newX, newY);

        if (isCollision(newHead, state)) {
            state.setGameOver(true);
            return;
        }

        state.getSnake().add(0, newHead);

        if (newHead.getX() == state.getFood().getX() && newHead.getY() == state.getFood().getY()) {
            state.setScore(state.getScore() + 10);
            generateFood(state);
        } else {
            state.getSnake().remove(state.getSnake().size() - 1);
        }
    }

    private boolean isCollision(SnakeState.Point newHead, SnakeState state) {
        if (newHead.getX() < 0 || newHead.getX() >= BOARD_WIDTH ||
            newHead.getY() < 0 || newHead.getY() >= BOARD_HEIGHT) {
            return true;
        }

        return state.getSnake().stream()
            .skip(1)
            .anyMatch(point -> 
                point.getX() == newHead.getX() && 
                point.getY() == newHead.getY()
            );
    }

    private void generateFood(SnakeState state) {
        Random rand = new Random();
        boolean isOnSnake;
        
        do {
            int x = rand.nextInt(BOARD_WIDTH);
            int y = rand.nextInt(BOARD_HEIGHT);
            SnakeState.Point food = new SnakeState.Point(x, y);
            
            isOnSnake = state.getSnake().stream()
                .anyMatch(point -> 
                    point.getX() == food.getX() && 
                    point.getY() == food.getY()
                );
            
            if (!isOnSnake) {
                state.setFood(food);
                return;
            }
        } while (true);
    }

    private void saveGameResult(SnakeState state) {
        GameResult result = new GameResult();
        result.setPlayerName(state.getPlayerName());
        result.setGameType("Snake");
        result.setScore(state.getScore());
        result.setDate(LocalDateTime.now());
        gameResultRepository.save(result);
    }
}