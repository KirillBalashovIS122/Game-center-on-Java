INSERT INTO game_result (player_name, game_type, score, date) 
SELECT 'Test Player', 'SNAKE', 100, CURRENT_TIMESTAMP 
WHERE NOT EXISTS (SELECT 1 FROM game_result WHERE player_name = 'Test Player' AND game_type = 'SNAKE');

INSERT INTO game_result (player_name, game_type, score, date) 
SELECT 'Test Player', 'TETRIS', 200, CURRENT_TIMESTAMP 
WHERE NOT EXISTS (SELECT 1 FROM game_result WHERE player_name = 'Test Player' AND game_type = 'TETRIS');

INSERT INTO games (name, description) 
SELECT 'Snake', 'Classic Snake game' 
WHERE NOT EXISTS (SELECT 1 FROM games WHERE name = 'Snake');

INSERT INTO games (name, description) 
SELECT 'Tetris', 'Classic Tetris game' 
WHERE NOT EXISTS (SELECT 1 FROM games WHERE name = 'Tetris');