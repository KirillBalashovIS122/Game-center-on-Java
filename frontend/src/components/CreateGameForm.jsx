import React, { useState } from 'react';
import axios from 'axios';

const CreateGameForm = () => {
    const [name, setName] = useState('');
    const [description, setDescription] = useState('');

    const handleSubmit = (e) => {
        e.preventDefault();
        axios.post('/api/games', { name, description })
            .then(response => {
                console.log("Game created:", response.data);
                setName('');
                setDescription('');
            })
            .catch(error => {
                console.error("Error creating game:", error);
            });
    };

    return (
        <form onSubmit={handleSubmit}>
            <h2>Create New Game</h2>
            <div>
                <label>Name:</label>
                <input
                    type="text"
                    value={name}
                    onChange={(e) => setName(e.target.value)}
                    required
                />
            </div>
            <div>
                <label>Description:</label>
                <textarea
                    value={description}
                    onChange={(e) => setDescription(e.target.value)}
                    required
                />
            </div>
            <button type="submit">Create Game</button>
        </form>
    );
};

export default CreateGameForm;