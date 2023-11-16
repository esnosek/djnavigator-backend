package dev.nos.djnavigator.utils.time;

import java.time.Instant;
import java.time.LocalDateTime;

public class ClockImpl implements Clock {

    private final java.time.Clock clock;


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
        throw new RuntimeException("only for testing");
    }
}
