import React, { useState, useEffect } from 'react';
import { GameService } from '../../../services/gameService';
import { useSound } from '../../../contexts/SoundContext';
import './style.css';

const SnakeBoard = ({ gameId, onGameOver }) => {
    const [gameState, setGameState] = useState(null);
    const [playerName, setPlayerName] = useState('');
    const [showNameModal, setShowNameModal] = useState(false);
    const { playSound } = useSound();

    useEffect(() => {
        const fetchGameState = async () => {
            try {
                const state = await GameService.getSnakeState(gameId);
                setGameState(state);
                
                if (state?.gameOver) {
                    playSound('game-over.mp3');
                    setShowNameModal(true);
                }
            } catch (error) {
                console.error('Ошибка получения состояния:', error);
            }
        };

        const interval = setInterval(fetchGameState, 200);
        return () => clearInterval(interval);
    }, [gameId, playSound]);

    useEffect(() => {
        const handleKeyPress = (e) => {
            if (!gameState?.gameOver) {
                const directionMap = {
                    ArrowUp: 'UP',
                    ArrowDown: 'DOWN',
                    ArrowLeft: 'LEFT',
                    ArrowRight: 'RIGHT'
                };
                
                if (directionMap[e.key]) {
                    GameService.sendSnakeAction(gameId, directionMap[e.key]);
                    playSound('snake-move.mp3');
                }
            }
        };

        window.addEventListener('keydown', handleKeyPress);
        return () => window.removeEventListener('keydown', handleKeyPress);
    }, [gameId, gameState, playSound]);

    const handleSaveResult = async () => {
        if (!playerName.trim()) return;
        
        try {
            await GameService.saveResult({
                playerName,
                score: gameState.score,
                gameType: 'Snake'
            });
            setShowNameModal(false);
            onGameOver();
        } catch (error) {
            console.error('Ошибка сохранения:', error);
        }
    };

    return (
        <div className="snake-board">
            {/* Модальное окно для ввода имени */}
            {showNameModal && (
                <div className="result-modal">
                    <div className="modal-content">
                        <h3>Ваш счет: {gameState?.score || 0}</h3>
                        <input
                            type="text"
                            placeholder="Введите имя"
                            value={playerName}
                            onChange={(e) => setPlayerName(e.target.value)}
                        />
                        <button 
                            className="save-btn"
                            onClick={handleSaveResult}
                        >
                            Сохранить
                        </button>
                    </div>
                </div>
            )}

            {/* Игровое поле */}
            <div className="game-header">
                <div className="score">Счет: {gameState?.score || 0}</div>
                <div className="controls">Управление: ← ↑ → ↓</div>
            </div>

            <div className="grid-container">
                {Array(20).fill().map((_, y) => (
                    <div key={y} className="grid-row">
                        {Array(20).fill().map((_, x) => {
                            const isSnake = gameState?.snake?.some(p => p.x === x && p.y === y);
                            const isHead = gameState?.snake?.[0]?.x === x && gameState?.snake?.[0]?.y === y;
                            const isFood = gameState?.food?.x === x && gameState?.food?.y === y;
                            
                            return (
                                <div
                                    key={x}
                                    className={`grid-cell 
                                        ${isSnake ? 'snake-body' : ''}
                                        ${isHead ? 'snake-head' : ''}
                                        ${isFood ? 'food' : ''}`}
                                />
                            );
                        })}
                    </div>
                ))}
            </div>
        </div>
    );
};

export default SnakeBoard;