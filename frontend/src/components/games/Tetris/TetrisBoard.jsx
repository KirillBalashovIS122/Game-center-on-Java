import React, { useState, useEffect, useCallback } from 'react';
import { GameService } from '../../../services/gameService';
import { useSound } from '../../../contexts/SoundContext';
import './style.css';

const SHAPES = [
    [[1, 1, 1, 1]],
    [[1, 1], [1, 1]],
    [[0, 1, 0], [1, 1, 1]],
    [[1, 0], [1, 0], [1, 1]],
    [[0, 1], [0, 1], [1, 1]],
    [[0, 1, 1], [1, 1, 0]],
    [[1, 1, 0], [0, 1, 1]]
];

const TetrisBoard = ({ gameId, onGameOver }) => {
    const [gameState, setGameState] = useState(null);
    const [playerName, setPlayerName] = useState('');
    const [dropSpeed, setDropSpeed] = useState(1000);
    const [lastDrop, setLastDrop] = useState(Date.now());
    const { playSound } = useSound();

    const updateGameState = useCallback(async () => {
        try {
            const state = await GameService.getTetrisState(gameId);
            setGameState(state);
            if (state?.gameOver) {
                playSound('gameover.mp3');
            }
        } catch (error) {
            console.error("Failed to get game state:", error);
        }
    }, [gameId, playSound]);

    useEffect(() => {
        const interval = setInterval(async () => {
            await updateGameState();
        }, 100);
        return () => clearInterval(interval);
    }, [updateGameState]);

    useEffect(() => {
        const handleKeyPress = (e) => {
            const actions = {
                ArrowUp: { action: 'ROTATE', sound: 'rotate.mp3' },
                KeyZ: { action: 'ROTATE', sound: 'rotate.mp3' },
                KeyX: { action: 'ROTATE', sound: 'rotate.mp3' },
                ArrowLeft: { action: 'MOVE_LEFT', sound: 'move.mp3' },
                ArrowRight: { action: 'MOVE_RIGHT', sound: 'move.mp3' },
                ArrowDown: { action: 'SOFT_DROP', sound: 'drop.mp3' },
                Space: { action: 'HARD_DROP', sound: 'harddrop.mp3' }
            };
            
            if (actions[e.code]) {
                GameService.sendTetrisAction(gameId, actions[e.code].action);
                playSound(actions[e.code].sound);
                e.preventDefault();
            }
        };

        window.addEventListener('keydown', handleKeyPress);
        return () => window.removeEventListener('keydown', handleKeyPress);
    }, [gameId, playSound]);

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

    const handleSave = async () => {
        if (!playerName.trim()) return;
        try {
            await GameService.saveResult({
                playerName: playerName.trim(),
                score: gameState.score,
                gameType: 'Tetris'
            });
            onGameOver();
        } catch (error) {
            console.error("Failed to save result:", error);
        }
    };

    const renderBoard = () => {
        if (!gameState) return null;
        
        const displayBoard = Array(20).fill().map(() => Array(10).fill(0));
        
        for (let y = 0; y < 20; y++) {
            for (let x = 0; x < 10; x++) {
                displayBoard[y][x] = gameState.board[y][x];
            }
        }

        if (gameState.currentPiece && !gameState.gameOver) {
            for (let i = 0; i < gameState.currentPiece.length; i++) {
                for (let j = 0; j < gameState.currentPiece[i].length; j++) {
                    const y = gameState.pieceY + i;
                    const x = gameState.pieceX + j;
                    if (y >= 0 && gameState.currentPiece[i][j] && x >= 0 && x < 10 && y < 20) {
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
            {gameState?.gameOver ? (
                <div className="game-over-modal">
                    <h2>Игра окончена! Счет: {gameState.score}</h2>
                    <input
                        type="text"
                        placeholder="Ваше имя"
                        value={playerName}
                        onChange={(e) => setPlayerName(e.target.value)}
                        maxLength={20}
                    />
                    <button onClick={handleSave}>Сохранить результат</button>
                </div>
            ) : (
                <>
                    <div className="score-board">Счет: {gameState?.score || 0}</div>
                    <div className="tetris-grid">{renderBoard()}</div>
                    <div className="next-piece">
                        Следующая:
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
                        <p>Управление:</p>
                        <ul>
                            <li>← → - Движение</li>
                            <li>↑, Z, X - Поворот</li>
                            <li>↓ - Ускорение</li>
                            <li>Пробел - Мгновенное падение</li>
                        </ul>
                    </div>
                </>
            )}
        </div>
    );
};

export default TetrisBoard;