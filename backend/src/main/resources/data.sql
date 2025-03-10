CREATE TABLE IF NOT EXISTS game_result (
    id SERIAL PRIMARY KEY,
    player_name VARCHAR(255) NOT NULL,
    game_type VARCHAR(50) NOT NULL,
    score INT NOT NULL,
    date TIMESTAMP NOT NULL
);