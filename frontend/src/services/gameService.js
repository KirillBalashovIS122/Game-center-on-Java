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
            console.log('Sent action:', direction, 'for game:', gameId);
        } catch (error) {
            console.error('Error sending snake action:', error);
            throw error;
        }
    },

    getSnakeState: async (gameId) => {
        try {
            const response = await api.get(`/snake/state?gameId=${gameId}`);
            console.log('Received game state:', response.data);
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
    }
};