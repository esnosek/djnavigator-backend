CREATE TABLE IF NOT EXISTS audio_analysis (
    id text PRIMARY KEY,
    created_date timestamp NOT NULL,
    data text NOT NULL,
    track_id text NOT NULL,
    FOREIGN KEY (track_id) REFERENCES track(id)
);
CREATE INDEX idx_audio_analysis_track_id ON audio_analysis(track_id);