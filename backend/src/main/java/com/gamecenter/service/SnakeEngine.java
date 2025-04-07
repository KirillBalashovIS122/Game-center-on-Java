package com.gamecenter.service;

import com.gamecenter.model.GameResult;
import com.gamecenter.model.SnakeState;
import com.gamecenter.repository.GameResultRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
@Transactional
public class SnakeEngine {
    private static final Logger logger = LoggerFactory.getLogger(SnakeEngine.class);
    private static final int BOARD_WIDTH = 20;
    private static final int BOARD_HEIGHT = 20;
    
    private final ConcurrentHashMap<String, SnakeState> games = new ConcurrentHashMap<>();
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
        logger.info("Created new game: {}", gameId);
        return gameId;
    }

    @Scheduled(fixedRate = 300)
    public void autoMoveSnakes() {
        logger.debug("Auto-moving snakes. Active games: {}", games.size());
        
        games.forEach((gameId, state) -> {
            synchronized(state) {
                if (!state.isGameOver()) {
                    logger.debug("Moving game: {}", gameId);
                    SnakeState newState = moveSnake(state);
                    games.put(gameId, newState);
                    
                    if (newState.isGameOver()) {
                        logger.info("Game over: {}. Score: {}", gameId, newState.getScore());
                        saveGameResult(newState);
                    }
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
            synchronized(state) {
                if (!isOppositeDirection(state.getDirection(), newDirection)) {
                    state.setDirection(newDirection);
                    state.setLastActivity(LocalDateTime.now());
                    logger.debug("Changed direction to {} for game: {}", direction, gameId);
                }
            }
            return Optional.of(state);
        } catch (IllegalArgumentException e) {
            logger.warn("Invalid direction: {}", direction);
            return Optional.empty();
        }
    }

    public Optional<SnakeState> getGameState(String gameId) {
        return Optional.ofNullable(games.get(gameId));
    }

    private SnakeState moveSnake(SnakeState state) {
        SnakeState newState = deepCopyState(state);
        
        // Calculate new head position with screen wrapping
        SnakeState.Point head = newState.getSnake().get(0);
        int newX = (head.getX() + newState.getDirection().dx + BOARD_WIDTH) % BOARD_WIDTH;
        int newY = (head.getY() + newState.getDirection().dy + BOARD_HEIGHT) % BOARD_HEIGHT;
        SnakeState.Point newHead = new SnakeState.Point(newX, newY);

        // Check for collisions (except tail)
        if (newState.getSnake().stream()
            .limit(newState.getSnake().size() - 1)
            .anyMatch(p -> p.equals(newHead))) {
            newState.setGameOver(true);
            return newState;
        }

        newState.getSnake().add(0, newHead);
        
        // Check if food was eaten
        if (newHead.equals(newState.getFood())) {
            newState.setScore(newState.getScore() + 10);
            generateFood(newState);
            logger.debug("Food eaten at ({},{}). New score: {}", newX, newY, newState.getScore());
        } else {
            newState.getSnake().remove(newState.getSnake().size() - 1);
        }
        
        newState.setLastActivity(LocalDateTime.now());
        return newState;
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

    private void saveGameResult(SnakeState state) {
        try {
            if (state.getPlayerName() == null || state.getPlayerName().isBlank()) {
                logger.error("Attempt to save result without player name");
                return;
            }

            GameResult result = new GameResult();
            result.setPlayerName(state.getPlayerName().trim());
            result.setGameType("Snake");
            result.setScore(state.getScore());
            result.setDate(LocalDateTime.now());
            
            gameResultRepository.saveAndFlush(result);
            logger.info("Saved result for {}: {}", result.getPlayerName(), result.getScore());
            
        } catch (DataIntegrityViolationException e) {
            logger.error("Data validation error: {}", e.getMostSpecificCause().getMessage());
        } catch (Exception e) {
            logger.error("Failed to save result: {}", e.getMessage());
            logger.debug("Stack trace:", e);
        }
    }

    private SnakeState deepCopyState(SnakeState original) {
        SnakeState copy = new SnakeState();
        copy.setSnake(new ArrayList<>(original.getSnake()));
        copy.setFood(original.getFood());
        copy.setDirection(original.getDirection());
        copy.setScore(original.getScore());
        copy.setGameOver(original.isGameOver());
        copy.setPlayerName(original.getPlayerName());
        copy.setLastActivity(original.getLastActivity());
        return copy;
    }

    private boolean isOppositeDirection(SnakeState.Direction current, SnakeState.Direction newDir) {
        return (current == SnakeState.Direction.UP && newDir == SnakeState.Direction.DOWN) ||
               (current == SnakeState.Direction.DOWN && newDir == SnakeState.Direction.UP) ||
               (current == SnakeState.Direction.LEFT && newDir == SnakeState.Direction.RIGHT) ||
               (current == SnakeState.Direction.RIGHT && newDir == SnakeState.Direction.LEFT);
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