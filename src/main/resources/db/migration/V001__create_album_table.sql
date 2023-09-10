CREATE TABLE IF NOT EXISTS album (
    id text PRIMARY KEY,
    created_date timestamp NOT NULL,
    name text NOT NULL,
    artists text NOT NULL,
    spotify_id text,
    image_path text
);
CREATE INDEX idx_album_name ON album(name);
CREATE INDEX idx_album_spotify_id ON album(spotify_id);