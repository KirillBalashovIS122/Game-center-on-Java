import React, { useEffect, useState } from 'react';
import { GameService } from '../../services/gameService';
import './Leaderboard.css';

const Leaderboard = ({ gameType }) => {
  const [leaderboard, setLeaderboard] = useState([]);

  useEffect(() => {
    const loadLeaderboard = async () => {
      const data = await GameService.getLeaderboard(gameType);
      setLeaderboard(data);
    };
    loadLeaderboard();
  }, [gameType]);

  return (
    <div className="leaderboard">
      <h2>ТАБЛИЦА ЛИДЕРОВ: {gameType === 'Tetris' ? 'ТЕТРИС' : 'ЗМЕЙКА'}</h2>
      <table>
        <thead>
          <tr>
            <th>ИГРОК</th>
            <th>СЧЁТ</th>
            <th>ДАТА</th>
          </tr>
        </thead>
        <tbody>
          {leaderboard.map((entry, index) => (
            <tr key={index}>
              <td>{entry.playerName}</td>
              <td>{entry.score}</td>
              <td>{new Date(entry.date).toLocaleDateString('ru-RU')}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

export default Leaderboard;