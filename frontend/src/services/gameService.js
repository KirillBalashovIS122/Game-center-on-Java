import { api } from './api';

export const GameService = {
    createSnakeGame: async () => {
        try {
            const response = await api.post('/snake/new');
            console.log('Created new snake game:', response.data);
            return response.data;
        } catch (error) {
            console.error('Error creating snake game:', error);
            throw error;
        }
    },

    sendSnakeAction: async (gameId, direction) => {
        try {
            await api.post('/snake/move', { 
                gameId, 
                direction 
            });
            console.log('Sent snake action:', direction, 'for game:', gameId);
        } catch (error) {
            console.error('Error sending snake action:', error);
            throw error;
        }
    },

    getSnakeState: async (gameId) => {
        try {
            const response = await api.get(`/snake/state?gameId=${gameId}`);
            console.log('Received snake state:', response.data);
            return response.data;
        } catch (error) {
            console.error('Error fetching snake state:', error);
            return {
                snake: [],
                food: {x: 0, y: 0},
                score: 0,
                gameOver: true,
                direction: 'RIGHT'
            };
        }
    },

    createTetrisGame: async () => {
        try {
            const response = await api.post('/tetris/new');
            console.log('Created new Tetris game:', response.data);
            return response.data;
        } catch (error) {
            console.error('Error creating Tetris game:', error);
            throw error;
        }
    },

    sendTetrisAction: async (gameId, action) => {
        try {
            await api.post('/tetris/action', null, {
                params: { 
                    gameId,
                    action 
                }
            });
            console.log('Sent Tetris action:', action, 'for game:', gameId);
        } catch (error) {
            console.error('Error sending Tetris action:', error);
            throw error;
        }
    },

    getTetrisState: async (gameId) => {
        try {
            const response = await api.get('/tetris/state', {
                params: { gameId }
            });
            console.log('Received Tetris state:', response.data);
            return response.data;
        } catch (error) {
            console.error('Error fetching Tetris state:', error);
            return {
                board: Array(20).fill().map(() => Array(10).fill(0)),
                currentPiece: null,
                score: 0,
                gameOver: true
            };
        }
    },

    saveResult: async (result) => {
        try {
            await api.post('/results', {
                playerName: result.playerName,
                score: result.score,
                gameType: result.gameType
            });
            console.log('Saved result for:', result.playerName);
        } catch (error) {
            console.error('Error saving game result:', error);
            throw error;
        }
    },

    getLeaderboard: async (gameType) => {
        try {
            const response = await api.get('/results', {
                params: { gameType }
            });
            return response.data;
        } catch (error) {
            console.error('Error fetching leaderboard:', error);
            return [];
        }
    },

    getGameStatus: async () => {
        try {
            const response = await api.get('/status');
            return response.data;
        } catch (error) {
            console.error('Error fetching game status:', error);
            return 'Service unavailable';
        }
    }
};