import React, { useState, useEffect } from 'react';
import { GameService } from '../../../services/gameService';
import { useSound } from '../../../contexts/SoundContext';
import './style.css';

const SnakeBoard = ({ gameId, onGameOver }) => {
    const [gameState, setGameState] = useState(null);
    const { playSound } = useSound();

    useEffect(() => {
        const fetchState = async () => {
            try {
                const state = await GameService.getSnakeState(gameId);
                setGameState(state);
                if (state?.gameOver) {
                    playSound('game-over.mp3');
                    onGameOver();
                }
            } catch (error) {
                console.error('Ошибка загрузки состояния:', error);
            }
        };

        const interval = setInterval(fetchState, 200);
        return () => clearInterval(interval);
    }, [gameId, onGameOver, playSound]);

    useEffect(() => {
        const handleKeyPress = (e) => {
            const directions = {
                ArrowUp: 'UP',
                ArrowDown: 'DOWN',
                ArrowLeft: 'LEFT',
                ArrowRight: 'RIGHT'
            };
            
            if (directions[e.key] && gameState && !gameState.gameOver) {
                const currentDirection = gameState.direction;
                const newDirection = directions[e.key];
                if (
                    (currentDirection === 'UP' && newDirection === 'DOWN') ||
                    (currentDirection === 'DOWN' && newDirection === 'UP') ||
                    (currentDirection === 'LEFT' && newDirection === 'RIGHT') ||
                    (currentDirection === 'RIGHT' && newDirection === 'LEFT')
                ) return;

                GameService.sendSnakeAction(gameId, newDirection);
                playSound('snake-move.mp3');
            }
        };

        window.addEventListener('keydown', handleKeyPress);
        return () => window.removeEventListener('keydown', handleKeyPress);
    }, [gameId, gameState, playSound]);

    return (
        <div className="snake-container">
            <div className="header">
                <div className="score">Счёт: {gameState?.score || 0}</div>
                <div className="controls-hint">Управление: ← ↑ → ↓</div>
            </div>
            
            {gameState?.gameOver ? (
                <div className="game-over">
                    <h2>Игра окончена! 🎯</h2>
                    <button onClick={onGameOver} className="menu-btn">
                        В главное меню
                    </button>
                </div>
            ) : (
                <div className="game-grid">
                    {Array(20).fill().map((_, y) => (
                        <div key={y} className="grid-row">
                            {Array(20).fill().map((_, x) => {
                                const isSnake = gameState?.snake?.some(p => p.x === x && p.y === y);
                                const isFood = gameState?.food?.x === x && gameState?.food?.y === y;
                                return (
                                    <div 
                                        key={x} 
                                        className={`grid-cell ${isSnake ? 'snake' : ''} ${isFood ? 'food' : ''}`}
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