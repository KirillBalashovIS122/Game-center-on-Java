import React, { useEffect, useState } from 'react';
import axios from 'axios';

const GameList = () => {
    const [games, setGames] = useState([]);

    useEffect(() => {
        axios.get('/api/games')
            .then(response => {
                console.log("Response data:", response.data);
                setGames(response.data);
            })
            .catch(error => {
                console.error("Error fetching games:", error);
            });
    }, []);

    return (
        <div>
            <h1>Games</h1>
            {games.length > 0 ? (
                <ul>
                    {games.map(game => (
                        <li key={game.id}>
                            <strong>{game.name}</strong>: {game.description}
                        </li>
                    ))}
                </ul>
            ) : (
                <p>No games found.</p>
            )}
        </div>
    );
};

export default GameList;