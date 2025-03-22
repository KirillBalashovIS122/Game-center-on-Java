import React, { useState } from 'react';
import { GameService } from '../../services/gameService';
import SnakeBoard from '../games/Snake/SnakeBoard';
import './GameMenu.css';

const GameMenu = () => {
    const [selectedGame, setSelectedGame] = useState(null);
    const [gameId, setGameId] = useState(null);

    const startGame = async (gameType) => {
        if (gameType === 'Tetris') {
            setSelectedGame('Tetris');
            return;
        }

        try {
            const response = await GameService.createSnakeGame();
            setGameId(response);
            setSelectedGame('Snake');
        } catch (error) {
            console.error('Ошибка создания игры:', error);
        }
    };

    return (
        <div className="game-menu">
            {!selectedGame ? (
                <>
                    <h1 className="title">Добро пожаловать в Игровой Центр!</h1>
                    <div className="game-buttons">
                        <button 
                            onClick={() => startGame('Snake')}
                            className="snake-btn"
                        >
                            🐍 Играть в Змейку
                        </button>
                        <button 
                            onClick={() => startGame('Tetris')}
                            className="tetris-btn"
                        >
                            🧊 Играть в Тетрис
                        </button>
                    </div>
                </>
            ) : selectedGame === 'Snake' ? (
                <SnakeBoard 
                    gameId={gameId} 
                    onGameOver={() => setSelectedGame(null)}
                />
            ) : (
                <div className="dev-message">
                    <h2>🎮 Тетрис в разработке!</h2>
                    <p>Я уссердно тружусь над созданием тетроиса!</p>
                    <button 
                        onClick={() => setSelectedGame(null)}
                        className="back-btn"
                    >
                        ← Вернуться
                    </button>
                </div>
            )}
        </div>
    );
};

export default GameMenu;