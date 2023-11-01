package dev.nos.djnavigator.config;

import dev.nos.djnavigator.time.Clock;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class TestClockImpl implements Clock {

    private Instant fixed;

    public TestClockImpl(Instant fixed) {
        this.fixed = fixed;
    }

    @Override
    public LocalDateTime now() {
        return LocalDateTime.ofInstant(fixed, ZoneId.systemDefault());
    }

    @Override
    public Instant instant() {
        return fixed;
    }

    @Override
    public void setFixed(Instant fixed) {
        this.fixed = fixed;
    }
}
