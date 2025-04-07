package com.gamecenter.service;

import com.gamecenter.model.GameResult;
import com.gamecenter.model.SnakeState;
import com.gamecenter.repository.GameResultRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class SnakeEngine {
    private static final Logger logger = LoggerFactory.getLogger(SnakeEngine.class);
    private static final int BOARD_WIDTH = 20;
    private static final int BOARD_HEIGHT = 20;
    private final Map<String, SnakeState> games = new HashMap<>();
    private final GameResultRepository gameResultRepository;

    public String createGame() {
        String gameId = UUID.randomUUID().toString();
        SnakeState state = new SnakeState();

        List<SnakeState.Point> initialSnake = new ArrayList<>();
        initialSnake.add(new SnakeState.Point(10, 10));
        initialSnake.add(new SnakeState.Point(9, 10));
        initialSnake.add(new SnakeState.Point(8, 10));
        
        state.setSnake(initialSnake);
        state.setDirection(SnakeState.Direction.RIGHT);
        state.setLastActivity(LocalDateTime.now());
        generateFood(state);
        games.put(gameId, state);
        return gameId;
    }

    @Scheduled(fixedRate = 300)
    public void autoMoveSnakes() {
        games.forEach((gameId, state) -> {
            if (!state.isGameOver()) {
                moveSnake(state);
                if (state.isGameOver()) {
                    logger.info("Game {} ended with score: {}", gameId, state.getScore());
                }
            }
        });
    }

    public Optional<SnakeState> handleAction(String gameId, String direction) {
        SnakeState state = games.get(gameId);
        if (state == null || state.isGameOver()) {
            return Optional.empty();
        }

        try {
            SnakeState.Direction newDirection = SnakeState.Direction.valueOf(direction);
            if (isOppositeDirection(state.getDirection(), newDirection)) {
                return Optional.of(state);
            }
            
            SnakeState newState = new SnakeState();
            newState.setSnake(new ArrayList<>(state.getSnake()));
            newState.setFood(state.getFood());
            newState.setDirection(newDirection);
            newState.setScore(state.getScore());
            newState.setGameOver(state.isGameOver());
            newState.setPlayerName(state.getPlayerName());
            newState.setLastActivity(LocalDateTime.now());
            
            games.put(gameId, newState);
            return Optional.of(newState);
        } catch (IllegalArgumentException e) {
            logger.warn("Invalid direction received: {}", direction);
            return Optional.empty();
        }
    }

    public Optional<SnakeState> getGameState(String gameId) {
        return Optional.ofNullable(games.get(gameId));
    }

    private void moveSnake(SnakeState state) {
        SnakeState.Point head = state.getSnake().get(0);
        int newX = head.getX() + state.getDirection().dx;
        int newY = head.getY() + state.getDirection().dy;
        SnakeState.Point newHead = new SnakeState.Point(newX, newY);

        if (isCollision(newHead, state)) {
            state.setGameOver(true);
            saveGameResult(state);
            return;
        }

        state.getSnake().add(0, newHead);
        if (newHead.equals(state.getFood())) {
            state.setScore(state.getScore() + 10);
            generateFood(state);
        } else {
            state.getSnake().remove(state.getSnake().size() - 1);
        }
        state.setLastActivity(LocalDateTime.now());
    }

    private void generateFood(SnakeState state) {
        Random rand = new Random();
        SnakeState.Point food;
        do {
            food = new SnakeState.Point(
                rand.nextInt(BOARD_WIDTH),
                rand.nextInt(BOARD_HEIGHT)
            );
        } while (state.getSnake().contains(food));
        state.setFood(food);
    }

    private boolean isCollision(SnakeState.Point point, SnakeState state) {
        if (point.getX() < 0 || point.getX() >= BOARD_WIDTH ||
            point.getY() < 0 || point.getY() >= BOARD_HEIGHT) {
            return true;
        }
        
        return state.getSnake().stream()
            .skip(1)
            .anyMatch(p -> p.equals(point));
    }

    private boolean isOppositeDirection(SnakeState.Direction current, SnakeState.Direction newDir) {
        return (current == SnakeState.Direction.UP && newDir == SnakeState.Direction.DOWN) ||
               (current == SnakeState.Direction.DOWN && newDir == SnakeState.Direction.UP) ||
               (current == SnakeState.Direction.LEFT && newDir == SnakeState.Direction.RIGHT) ||
               (current == SnakeState.Direction.RIGHT && newDir == SnakeState.Direction.LEFT);
    }

    private void saveGameResult(SnakeState state) {
        if (state.getPlayerName() == null || state.getPlayerName().isBlank()) {
            logger.warn("Attempted to save result without player name");
            return;
        }

        GameResult result = new GameResult();
        result.setPlayerName(state.getPlayerName());
        result.setGameType("Snake");
        result.setScore(state.getScore());
        result.setDate(LocalDateTime.now());
        
        try {
            gameResultRepository.save(result);
            logger.info("Saved result for player: {}, score: {}", 
                state.getPlayerName(), state.getScore());
        } catch (Exception e) {
            logger.error("Failed to save game result", e);
        }
    }

    @Scheduled(fixedRate = 60_000)
    public void cleanupOldGames() {
        int initialSize = games.size();
        games.entrySet().removeIf(entry -> 
            entry.getValue().isGameOver() && 
            entry.getValue().getLastActivity().isBefore(LocalDateTime.now().minusHours(1))
        );
        if (games.size() != initialSize) {
            logger.info("Cleaned up {} old games", initialSize - games.size());
        }
    }
}