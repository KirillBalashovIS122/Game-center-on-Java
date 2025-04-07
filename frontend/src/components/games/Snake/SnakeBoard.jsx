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
    const [gameStatus, setGameStatus] = useState('playing');
    const animationFrameRef = useRef();
    const lastUpdateTimeRef = useRef(0);
    const gameLoopInterval = 100;

    useEffect(() => {
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
                console.error('Error fetching game state:', error);
            }
        };

        const gameLoop = (timestamp) => {
            if (timestamp - lastUpdateTimeRef.current > gameLoopInterval) {
                fetchGameState();
                lastUpdateTimeRef.current = timestamp;
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
            if (gameStatus !== 'playing') return;

            const directionMap = {
                'ArrowUp': 'UP',
                'ArrowDown': 'DOWN',
                'ArrowLeft': 'LEFT',
                'ArrowRight': 'RIGHT'
            };

            if (directionMap[e.key]) {
                GameService.sendSnakeAction(gameId, directionMap[e.key]);
                e.preventDefault();
            }
        };

        window.addEventListener('keydown', handleKeyDown);
        return () => window.removeEventListener('keydown', handleKeyDown);
    }, [gameId, gameStatus]);

    const handleGameOver = (state) => {
        setGameStatus('over');
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
            console.error('Error saving result:', error);
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

    const renderGrid = () => {
        const grid = [];
        for (let y = 0; y < 20; y++) {
            const row = [];
            for (let x = 0; x < 20; x++) {
                row.push(renderCell(x, y));
            }
            grid.push(
                <div key={y} className="grid-row">
                    {row}
                </div>
            );
        }
        return grid;
    };

    return (
        <div className="snake-board">
            {showNameModal && (
                <div className="result-modal">
                    <div className="modal-content">
                        <h3>Игра окончена! Ваш счёт: {score}</h3>
                        <input
                            type="text"
                            placeholder="Enter your name"
                            value={playerName}
                            onChange={(e) => setPlayerName(e.target.value)}
                            className="name-input"
                        />
                        <button 
                            onClick={handleSaveResult}
                            className="save-btn"
                        >
                            Сохранить
                        </button>
                    </div>
                </div>
            )}

            <div className="game-header">
                <div className="score">Счёт: {gameState?.score || 0}</div>
                <div className="controls">Управление: стрелки</div>
            </div>

            <div className="grid-container">
                {renderGrid()}
            </div>

            {gameStatus === 'over' && !showNameModal && (
                <button 
                    onClick={() => onGameOver()}
                    className="back-btn"
                >
                    Вернуться в меню
                </button>
            )}
        </div>
    );
};

export default SnakeBoard;