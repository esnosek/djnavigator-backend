package dev.nos.djnavigator.time;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class ClockImpl implements Clock {

    private java.time.Clock clock;


    public ClockImpl(java.time.Clock clock) {
        this.clock = clock;
    }


    @Override
    public LocalDateTime now() {
        return LocalDateTime.now(clock);
    }

    @Override
    public Instant instant() {
        return Instant.now(clock);
    }

    @Override
    public void setFixed(Instant instant) {
        clock = java.time.Clock.fixed(instant, ZoneId.systemDefault());
    }
}
