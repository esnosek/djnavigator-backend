CREATE TABLE IF NOT EXISTS mix (
    id text PRIMARY KEY,
    created_date timestamp NOT NULL,
    left_turntable_track_id text NOT NULL,
    left_turntable_pitch numeric(6,2) NOT NULL,
    right_turntable_track_id text NOT NULL,
    right_turntable_pitch numeric(6,2) NOT NULL,
    FOREIGN KEY (left_turntable_track_id) REFERENCES track (id),
    FOREIGN KEY (right_turntable_track_id) REFERENCES track (id)
);
