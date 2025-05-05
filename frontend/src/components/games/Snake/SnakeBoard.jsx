import React, { useState, useEffect, useRef } from 'react';
import { GameService } from '../../../services/gameService';
import './style.css';

const SnakeBoard = ({ gameId, onGameOver }) => {
    const [gameState, setGameState] = useState({
        snake: [{x: 10, y: 10}, {x: 9, y: 10}, {x: 8, y: 10}],
        food: {x: 5, y: 5},
        score: 0,
        gameOver: false,
        direction: 'RIGHT'
    });
    const [playerName, setPlayerName] = useState('');
    const [showNameModal, setShowNameModal] = useState(false);
    const [score, setScore] = useState(0);
    const animationFrameRef = useRef();
    const lastDirectionRef = useRef('RIGHT');
    const pendingDirectionRef = useRef(null);

    useEffect(() => {
        let lastUpdate = performance.now();
        const UPDATE_INTERVAL = 100;

        const fetchGameState = async () => {
            try {
                const state = await GameService.getSnakeState(gameId);
                if (state) {
                    setGameState(prev => ({
                        ...prev,
                        ...state,
                        snake: state.snake || prev.snake,
                        food: state.food || prev.food,
                        direction: state.direction || prev.direction
                    }));
                    
                    if (state.gameOver) {
                        handleGameOver(state);
                    }
                }
            } catch (error) {
                console.error('Ошибка получения состояния игры:', error);
            }
        };

        const gameLoop = (timestamp) => {
            if (timestamp - lastUpdate >= UPDATE_INTERVAL) {
                fetchGameState();
                lastUpdate = timestamp;
            }
            animationFrameRef.current = requestAnimationFrame(gameLoop);
        };

        animationFrameRef.current = requestAnimationFrame(gameLoop);

        return () => {
            cancelAnimationFrame(animationFrameRef.current);
        };
    }, [gameId]);

    useEffect(() => {
        const handleKeyDown = (e) => {
            const directionMap = {
                'ArrowUp': 'UP',
                'ArrowDown': 'DOWN',
                'ArrowLeft': 'LEFT',
                'ArrowRight': 'RIGHT'
            };

            const newDirection = directionMap[e.key];
            if (!newDirection) return;

            const opposites = {
                'UP': 'DOWN',
                'DOWN': 'UP',
                'LEFT': 'RIGHT',
                'RIGHT': 'LEFT'
            };

            if (newDirection !== opposites[lastDirectionRef.current]) {
                pendingDirectionRef.current = newDirection;
                GameService.sendSnakeAction(gameId, newDirection);
                lastDirectionRef.current = newDirection;
                e.preventDefault();
            }
        };

        window.addEventListener('keydown', handleKeyDown, { passive: false });
        return () => window.removeEventListener('keydown', handleKeyDown);
    }, [gameId]);

    const handleGameOver = (state) => {
        setScore(state?.score || 0);
        setShowNameModal(true);
    };

    const handleSaveResult = async () => {
        if (!playerName.trim()) return;
        
        try {
            await GameService.saveResult({
                playerName,
                score,
                gameType: 'Snake'
            });
            setShowNameModal(false);
            onGameOver();
        } catch (error) {
            console.error('Ошибка сохранения результата:', error);
        }
    };

    const renderCell = (x, y) => {
        const isSnake = gameState.snake?.some(p => p.x === x && p.y === y);
        const isHead = gameState.snake?.[0]?.x === x && gameState.snake?.[0]?.y === y;
        const isFood = gameState.food?.x === x && gameState.food?.y === y;

        let className = 'grid-cell';
        if (isSnake) className += ' snake-body';
        if (isHead) className += ' snake-head';
        if (isFood) className += ' food';

        return <div key={`${x}-${y}`} className={className} />;
    };

    return (
        <div className="snake-board">
            {showNameModal && (
                <div className="result-modal">
                    <div className="modal-content">
                        <h2>ИГРА ОКОНЧЕНА</h2>
                        <p className="final-score">ВАШ СЧЁТ: {score}</p>
                        <input
                            type="text"
                            placeholder="ВВЕДИТЕ ВАШЕ ИМЯ"
                            value={playerName}
                            onChange={(e) => setPlayerName(e.target.value)}
                            maxLength={12}
                        />
                        <div className="modal-buttons">
                            <button onClick={handleSaveResult} className="save-btn">
                                СОХРАНИТЬ
                            </button>
                            <button onClick={() => onGameOver()} className="back-btn">
                                В МЕНЮ
                            </button>
                        </div>
                    </div>
                </div>
            )}

            <div className="game-header">
                <div className="score">СЧЁТ: {gameState?.score || 0}</div>
                <div className="controls">ИСПОЛЬЗУЙТЕ СТРЕЛКИ</div>
            </div>

            <div className="grid-container">
                {Array(20).fill().map((_, y) => (
                    <div key={y} className="grid-row">
                        {Array(20).fill().map((_, x) => renderCell(x, y))}
                    </div>
                ))}
            </div>

            {gameState.gameOver && !showNameModal && (
                <button onClick={onGameOver} className="back-btn">
                    ВЕРНУТЬСЯ В МЕНЮ
                </button>
            )}
        </div>
    );
};

export default SnakeBoard;