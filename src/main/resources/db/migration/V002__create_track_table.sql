CREATE TABLE IF NOT EXISTS track (
    id text PRIMARY KEY,
    created_date timestamp NOT NULL,
    name text NOT NULL,
    artists text NOT NULL,
    album_id text,
    album_side char,
    tempo numeric(6,2),
    spotify_id text,
    FOREIGN KEY (album_id) REFERENCES album (id)
);
CREATE INDEX idx_track_spotify_id ON track(spotify_id);