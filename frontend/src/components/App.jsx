import React, { useState } from 'react';
import GameMenu from './components/common/GameMenu';
import Leaderboard from './components/common/Leaderboard';
import SoundToggle from './components/common/SoundToggle';
import { SoundProvider } from './contexts/SoundContext';
import './App.css';

function App() {
  const [showLeaderboard, setShowLeaderboard] = useState(false);
  const [selectedGameType, setSelectedGameType] = useState('Tetris');

  return (
    <SoundProvider>
      <div className="app">
        <nav>
          <button onClick={() => setShowLeaderboard(false)}>Игры</button>
          <button onClick={() => setShowLeaderboard(true)}>Таблица лидеров</button>
          <SoundToggle />
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
    </SoundProvider>
  );
}

export default App;