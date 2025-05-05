import React, { useState } from 'react';
import GameMenu from './common/GameMenu';
import Leaderboard from "./common/Leaderboard";
import '../App.css';

function App() {
  const [showLeaderboard, setShowLeaderboard] = useState(false);
  const [selectedGameType, setSelectedGameType] = useState('Tetris');

  return (
    <div className="app">
      <nav>
        <button onClick={() => setShowLeaderboard(false)}>Игры</button>
        <button onClick={() => setShowLeaderboard(true)}>Таблица лидеров</button>
      </nav>
      
      {showLeaderboard ? (
        <>
          <select 
            value={selectedGameType} 
            onChange={(e) => setSelectedGameType(e.target.value)}
          >
            <option value="Tetris">Тетрис</option>
            <option value="Snake">Змейка</option>
          </select>
          <Leaderboard gameType={selectedGameType} />
        </>
      ) : (
        <GameMenu />
      )}
    </div>
  );
}

export default App;