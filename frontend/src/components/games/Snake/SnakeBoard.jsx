import React, { useState, useEffect } from 'react';
import { GameService } from '../../../services/gameService';
import { useSound } from '../../../contexts/SoundContext';
import './style.css';

const SnakeBoard = ({ gameId, onGameOver }) => {
  const [gameState, setGameState] = useState(null);
  const [direction, setDirection] = useState('RIGHT');
  const { playSound } = useSound();

  useEffect(() => {
    const interval = setInterval(async () => {
      const state = await GameService.getSnakeState(gameId);
      setGameState(state);
      if (state?.gameOver) {
        clearInterval(interval);
      }
    }, 150);
    return () => clearInterval(interval);
  }, [gameId]);

  useEffect(() => {
    const handleKeyPress = (e) => {
      const directions = {
        ArrowUp: { direction: 'UP', sound: 'snake-move.mp3' },
        ArrowDown: { direction: 'DOWN', sound: 'snake-move.mp3' },
        ArrowLeft: { direction: 'LEFT', sound: 'snake-move.mp3' },
        ArrowRight: { direction: 'RIGHT', sound: 'snake-move.mp3' }
      };
      if (directions[e.key]) {
        setDirection(directions[e.key].direction);
        GameService.sendSnakeAction(gameId, directions[e.key].direction);
        playSound(directions[e.key].sound);
      }
    };

    window.addEventListener('keydown', handleKeyPress);
    return () => window.removeEventListener('keydown', handleKeyPress);
  }, [gameId, playSound]);

  return (
    <div className="snake-container">
      {gameState?.gameOver ? (
        <div className="game-over">
          <h2>Игра окончена! Счёт: {gameState.score}</h2>
          <button onClick={onGameOver}>В меню</button>
        </div>
      ) : (
        <div className="snake-grid">
          {Array(20).fill().map((_, y) => (
            <div key={y} className="row">
              {Array(20).fill().map((_, x) => {
                const isSnake = gameState?.snake.some(p => p.x === x && p.y === y);
                const isFood = gameState?.food.x === x && gameState?.food.y === y;
                return (
                  <div 
                    key={x} 
                    className={`cell ${isSnake ? 'snake' : ''} ${isFood ? 'food' : ''}`}
                  />
                );
              })}
            </div>
          ))}
        </div>
      )}
    </div>
  );
};

export default SnakeBoard;