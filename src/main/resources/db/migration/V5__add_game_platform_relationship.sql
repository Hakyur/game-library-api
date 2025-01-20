CREATE TABLE game_platform (
    game_id INTEGER NOT NULL,
    platform_id INTEGER NOT NULL,
    PRIMARY KEY (game_id, platform_id),
    CONSTRAINT fk_game FOREIGN KEY (game_id) REFERENCES games (id) ON DELETE CASCADE,
    CONSTRAINT fk_platform FOREIGN KEY (platform_id) REFERENCES platforms (id) ON DELETE CASCADE
);