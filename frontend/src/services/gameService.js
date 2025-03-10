import { api } from './api';

export const GameService = {
  createTetrisGame: async () => {
    const response = await api.post('/tetris/new');
    return response.data;
  },

  sendTetrisAction: async (gameId, action) => {
    await api.post(`/tetris/action?gameId=${gameId}&action=${action}`);
  },

  getTetrisState: async (gameId) => {
    const response = await api.get(`/tetris/state?gameId=${gameId}`);
    return response.data;
  },

  createSnakeGame: async () => {
    const response = await api.post('/snake/new');
    return response.data;
  },

  sendSnakeAction: async (gameId, direction) => {
    await api.post(`/snake/move?gameId=${gameId}&direction=${direction}`);
  },

  getSnakeState: async (gameId) => {
    const response = await api.get(`/snake/state?gameId=${gameId}`);
    return response.data;
  },

  saveResult: async (gameType, score, playerName) => {
    await api.post('/results', { gameType, score, playerName });
  },

  getLeaderboard: async (gameType) => {
    const response = await api.get(`/results?gameType=${gameType}`);
    return response.data;
  }
};