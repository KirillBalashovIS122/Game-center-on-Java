import React, { useState } from 'react';
import { GameService } from '../../services/gameService';
import SnakeBoard from '../games/Snake/SnakeBoard';
import TetrisBoard from '../games/Tetris/TetrisBoard';
import './GameMenu.css';

const GameMenu = () => {
    const [selectedGame, setSelectedGame] = useState(null);
    const [gameId, setGameId] = useState(null);

    const startGame = async (gameType) => {
        try {
            let newGameId;
            if (gameType === 'Snake') {
                newGameId = await GameService.createSnakeGame();
            } else if (gameType === 'Tetris') {
                newGameId = await GameService.createTetrisGame();
            }
            
            setGameId(newGameId);
            setSelectedGame(gameType);
        } catch (error) {
            console.error('Ошибка запуска игры:', error);
        }
    };

    return (
        <div className="game-menu-container">
            {!selectedGame ? (
                <div className="main-menu">
                    <h1 className="main-title">ИГРОВОЙ ЦЕНТР</h1>
                    <div className="game-buttons">
                        <button
                            className="snake-btn"
                            onClick={() => startGame('Snake')}
                        >
                            ЗМЕЙКА
                        </button>
                        <button
                            className="tetris-btn"
                            onClick={() => startGame('Tetris')}
                        >
                            ТЕТРИС
                        </button>
                    </div>
                </div>
            ) : selectedGame === 'Snake' ? (
                <SnakeBoard 
                    gameId={gameId} 
                    onGameOver={() => setSelectedGame(null)}
                />
            ) : selectedGame === 'Tetris' ? (
                <TetrisBoard 
                    gameId={gameId}
                    onGameOver={() => setSelectedGame(null)}
                />
            ) : null}
        </div>
    );
};

export default GameMenu;