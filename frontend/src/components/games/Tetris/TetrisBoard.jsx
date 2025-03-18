import React, { useState, useEffect } from 'react';
import { GameService } from '../../../services/gameService';
import { useSound } from '../../../contexts/SoundContext';
import './style.css';

const TetrisBoard = ({ gameId, onGameOver }) => {
  const [gameState, setGameState] = useState(null);
  const [playerName, setPlayerName] = useState('');
  const { playSound } = useSound();

  useEffect(() => {
    const interval = setInterval(async () => {
      const state = await GameService.getTetrisState(gameId);
      setGameState(state);
      if (state?.gameOver) {
        clearInterval(interval);
      }
    }, 100);
    return () => clearInterval(interval);
  }, [gameId]);

  useEffect(() => {
    const handleKeyPress = (e) => {
      const actions = {
        ArrowLeft: { action: 'MOVE_LEFT', sound: 'tetris-move.mp3' },
        ArrowRight: { action: 'MOVE_RIGHT', sound: 'tetris-move.mp3' },
        ArrowDown: { action: 'SOFT_DROP', sound: 'tetris-drop.mp3' },
        KeyZ: { action: 'ROTATE_LEFT', sound: 'tetris-rotate.mp3' },
        KeyX: { action: 'ROTATE_RIGHT', sound: 'tetris-rotate.mp3' }
      };
      if (actions[e.code]) {
        GameService.sendTetrisAction(gameId, actions[e.code].action);
        playSound(actions[e.code].sound);
      }
    };

    window.addEventListener('keydown', handleKeyPress);
    return () => window.removeEventListener('keydown', handleKeyPress);
  }, [gameId, playSound]);

  const handleSave = async () => {
    await GameService.saveResult('Tetris', gameState.score, playerName);
    onGameOver();
  };

  return (
    <div className="game-container">
      {gameState?.gameOver ? (
        <div className="game-over">
          <h2>Игра окончена! Счёт: {gameState.score}</h2>
          <input 
            type="text" 
            placeholder="Ваше имя"
            onChange={(e) => setPlayerName(e.target.value)}
          />
          <button onClick={handleSave}>Сохранить результат</button>
        </div>
      ) : (
        <div className="tetris-grid">
          {gameState?.board.map((row, y) => (
            <div key={y} className="row">
              {row.map((cell, x) => (
                <div 
                  key={x} 
                  className={`cell ${cell ? 'filled' : ''}`}
                />
              ))}
            </div>
          ))}
        </div>
      )}
    </div>
  );
};

export default TetrisBoard;