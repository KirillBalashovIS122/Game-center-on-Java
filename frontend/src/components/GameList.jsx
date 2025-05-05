import React, { useEffect, useState } from 'react';
import axios from 'axios';

const GameList = () => {
    const [games, setGames] = useState([]);

    useEffect(() => {
        axios.get('/api/games')
            .then(response => {
                console.log("Данные ответа:", response.data);
                setGames(response.data);
            })
            .catch(error => {
                console.error("Ошибка получения списка игр:", error);
            });
    }, []);

    return (
        <div>
            <h1>Список игр</h1>
            {games.length > 0 ? (
                <ul>
                    {games.map(game => (
                        <li key={game.id}>
                            <strong>{game.name}</strong>: {game.description}
                        </li>
                    ))}
                </ul>
            ) : (
                <p>Игры не найдены</p>
            )}
        </div>
    );
};

export default GameList;