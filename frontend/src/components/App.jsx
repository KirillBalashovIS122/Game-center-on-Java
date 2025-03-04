import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import GameList from './components/GameList';
import CreateGameForm from './components/CreateGameForm';

const App = () => (
    <Router>
        <Routes>
            <Route path="/" element={<GameList />} />
            <Route path="/create-game" element={<CreateGameForm />} />
        </Routes>
    </Router>
);

export default App;