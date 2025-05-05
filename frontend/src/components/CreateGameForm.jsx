import React, { useState } from 'react';
import axios from 'axios';

const CreateGameForm = () => {
    const [name, setName] = useState('');
    const [description, setDescription] = useState('');

    const handleSubmit = (e) => {
        e.preventDefault();
        axios.post('/api/games', { name, description })
            .then(response => {
                console.log("Игра создана:", response.data);
                setName('');
                setDescription('');
            })
            .catch(error => {
                console.error("Ошибка создания игры:", error);
            });
    };

    return (
        <form onSubmit={handleSubmit}>
            <h2>Создать новую игру</h2>
            <div>
                <label>Название:</label>
                <input
                    type="text"
                    value={name}
                    onChange={(e) => setName(e.target.value)}
                    required
                />
            </div>
            <div>
                <label>Описание:</label>
                <textarea
                    value={description}
                    onChange={(e) => setDescription(e.target.value)}
                    required
                />
            </div>
            <button type="submit">Создать игру</button>
        </form>
    );
};

export default CreateGameForm;