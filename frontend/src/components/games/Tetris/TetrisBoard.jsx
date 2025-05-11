import React, { useState, useEffect, useCallback } from 'react';
import { GameService } from '../../../services/gameService';
import './style.css';

const SHAPES = [
    [[1, 1, 1, 1]], // I
    [[1, 1], [1, 1]], // O
    [[0, 1, 0], [1, 1, 1]], // T
    [[1, 0], [1, 0], [1, 1]], // L
    [[0, 1], [0, 1], [1, 1]], // J
    [[0, 1, 1], [1, 1, 0]], // S
    [[1, 1, 0], [0, 1, 1]] // Z
];

const TetrisBoard = ({ gameId, onGameOver }) => {
    const [gameState, setGameState] = useState(null);
    const [playerName, setPlayerName] = useState('');
    const [showNameModal, setShowNameModal] = useState(false);
    const [score, setScore] = useState(0);
    const [dropSpeed, setDropSpeed] = useState(1000);
    const [lastDrop, setLastDrop] = useState(Date.now());

    const updateGameState = useCallback(async () => {
        try {
            const state = await GameService.getTetrisState(gameId);
            console.log('Game State:', state);
            setGameState(state);
            if (state.gameOver) {
                handleGameOver(state);
            }
        } catch (error) {
            console.error("Не удалось получить состояние игры:", error);
        }
    }, [gameId]);

    useEffect(() => {
        const interval = setInterval(updateGameState, 100);
        return () => clearInterval(interval);
    }, [updateGameState]);

    useEffect(() => {
        const handleKeyPress = (e) => {
            const actions = {
                ArrowUp: 'ROTATE',
                KeyZ: 'ROTATE',
                KeyX: 'ROTATE',
                ArrowLeft: 'MOVE_LEFT',
                ArrowRight: 'MOVE_RIGHT',
                ArrowDown: 'SOFT_DROP',
                Space: 'HARD_DROP'
            };
            
            if (actions[e.code]) {
                GameService.sendTetrisAction(gameId, actions[e.code]);
                e.preventDefault();
            }
        };

        window.addEventListener('keydown', handleKeyPress);
        return () => window.removeEventListener('keydown', handleKeyPress);
    }, [gameId]);

    useEffect(() => {
        const autoDrop = setInterval(async () => {
            if (!gameState?.gameOver) {
                const now = Date.now();
                if (now - lastDrop > dropSpeed) {
                    await GameService.sendTetrisAction(gameId, 'SOFT_DROP');
                    setLastDrop(now);
                    
                    if (gameState?.score > 1000) setDropSpeed(800);
                    if (gameState?.score > 2000) setDropSpeed(600);
                    if (gameState?.score > 3000) setDropSpeed(400);
                }
            }
        }, 100);

        return () => clearInterval(autoDrop);
    }, [gameId, gameState, lastDrop, dropSpeed]);

    const handleGameOver = (state) => {
        setScore(state?.score || 0);
        setShowNameModal(true);
        console.log('Game Over triggered with state:', state);
    };

    const handleSaveResult = async () => {
        if (!playerName.trim()) return;
        try {
            await GameService.saveResult({
                playerName: playerName.trim(),
                score: score,
                gameType: 'Tetris'
            });
            setShowNameModal(false);
            onGameOver();
        } catch (error) {
            console.error("Не удалось сохранить результат:", error);
        }
    };

    const renderBoard = () => {
        if (!gameState) return null;
        
        const displayBoard = Array(20).fill().map(() => Array(10).fill(0));
        
        for (let y = 0; y < 20; y++) {
            for (let x = 0; x < 10; x++) {
                if (gameState.board[y][x]) displayBoard[y][x] = 1;
            }
        }

        if (gameState.currentPiece && !gameState.gameOver) {
            console.log('Current Piece:', gameState.currentPiece, 'at (', gameState.pieceX, ',', gameState.pieceY, ')');
            for (let i = 0; i < gameState.currentPiece.length; i++) {
                for (let j = 0; j < gameState.currentPiece[i].length; j++) {
                    const y = gameState.pieceY + i;
                    const x = gameState.pieceX + j;
                    if (y >= 0 && y < 20 && x >= 0 && x < 10 && gameState.currentPiece[i][j]) {
                        displayBoard[y][x] = 2;
                    }
                }
            }
        }

        return displayBoard.map((row, y) => (
            <div key={y} className="tetris-row">
                {row.map((cell, x) => (
                    <div
                        key={x}
                        className={`tetris-cell ${
                            cell === 2 ? 'active' : 
                            cell === 1 ? 'filled' : ''
                        }`}
                    />
                ))}
            </div>
        ));
    };

    return (
        <div className="tetris-container">
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
            
            <div className="score-board">СЧЁТ: {gameState?.score || 0}</div>
            <div className="tetris-grid">{renderBoard()}</div>
            <div className="next-piece">
                <span>СЛЕДУЮЩАЯ ФИГУРА:</span>
                <div className="preview">
                    {gameState?.nextPiece !== undefined && SHAPES[gameState.nextPiece].map((row, i) => (
                        <div key={i} className="preview-row">
                            {row.map((cell, j) => (
                                <div key={j} className={`preview-cell ${cell ? 'filled' : ''}`} />
                            ))}
                        </div>
                    ))}
                </div>
            </div>
            <div className="controls-info">
                <p>УПРАВЛЕНИЕ:</p>
                <ul>
                    <li>← → : ПЕРЕМЕЩЕНИЕ</li>
                    <li>↑, Z, X : ПОВОРОТ</li>
                    <li>↓ : МЯГКОЕ ПАДЕНИЕ</li>
                    <li>ПРОБЕЛ : ЖЁСТКОЕ ПАДЕНИЕ</li>
                </ul>
            </div>
        </div>
    );
};

export default TetrisBoard;