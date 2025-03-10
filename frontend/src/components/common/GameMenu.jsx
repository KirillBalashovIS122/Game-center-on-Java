import React, { useState } from 'react';
import { GameService } from '../../services/gameService';
import TetrisBoard from '../games/Tetris/TetrisBoard';
import SnakeBoard from '../games/Snake/SnakeBoard';

const GameMenu = () => {
  const [selectedGame, setSelectedGame] = useState(null);
  const [gameId, setGameId] = useState(null);

  const startGame = async (gameType) => {
    const response = gameType === 'Tetris' 
      ? await GameService.createTetrisGame()
      : await GameService.createSnakeGame();
    setGameId(response);
    setSelectedGame(gameType);
  };

  return (
    <div className="game-menu">
      {!selectedGame ? (
        <>
          <h1>Добро пожаловать в Игровой Центр!</h1>
          <div className="game-buttons">
            <button onClick={() => startGame('Tetris')}>Играть в Тетрис</button>
            <button onClick={() => startGame('Snake')}>Играть в Змейку</button>
          </div>
        </>
      ) : selectedGame === 'Tetris' ? (
        <TetrisBoard 
          gameId={gameId} 
          onGameOver={() => setSelectedGame(null)}
        />
      ) : (
        <SnakeBoard 
          gameId={gameId}
          onGameOver={() => setSelectedGame(null)}
        />
      )}
    </div>
  );
};

export default GameMenu;