import React, { useEffect, useState } from 'react';
import { GameService } from '../../services/gameService';

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
      <h2>Таблица лидеров: {gameType}</h2>
      <table>
        <thead>
          <tr>
            <th>Имя</th>
            <th>Очки</th>
            <th>Дата</th>
          </tr>
        </thead>
        <tbody>
          {leaderboard.map((entry, index) => (
            <tr key={index}>
              <td>{entry.playerName}</td>
              <td>{entry.score}</td>
              <td>{new Date(entry.date).toLocaleDateString()}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

export default Leaderboard;