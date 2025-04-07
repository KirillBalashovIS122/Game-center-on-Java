import React, { useState } from 'react';
import { GameService } from '../../services/gameService';
import SnakeBoard from '../games/Snake/SnakeBoard';
import './GameMenu.css';

const GameMenu = () => {
    const [selectedGame, setSelectedGame] = useState(null);
    const [gameId, setGameId] = useState(null);

    const startGame = async (gameType) => {
        try {
            if (gameType === 'Snake') {
                const newGameId = await GameService.createSnakeGame();
                setGameId(newGameId);
                setSelectedGame('Snake');
            } else {
                setSelectedGame('Tetris');
            }
        } catch (error) {
            console.error('Ошибка запуска игры:', error);
        }
    };

    return (
        <div className="game-menu-container">
            {!selectedGame ? (
                <div className="main-menu">
                    <h1 className="main-title">🎮 Игровой Центр</h1>
                    <div className="game-buttons">
                        <button
                            className="snake-btn"
                            onClick={() => startGame('Snake')}
                        >
                            Новая игра: Змейка
                        </button>
                        <button
                            className="tetris-btn"
                            onClick={() => startGame('Tetris')}
                        >
                            Тетрис (скоро)
                        </button>
                    </div>
                </div>
            ) : selectedGame === 'Snake' ? (
                <SnakeBoard 
                    gameId={gameId} 
                    onGameOver={() => setSelectedGame(null)}
                />
            ) : (
                <div className="coming-soon">
                    <h2> В разработке</h2>
                    <button 
                        className="back-btn"
                        onClick={() => setSelectedGame(null)}
                    >
                        Назад в меню
                    </button>
                </div>
            )}
        </div>
    );
};

export default GameMenu;