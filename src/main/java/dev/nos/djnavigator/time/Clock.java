package dev.nos.djnavigator.time;

import java.time.Instant;
import java.time.LocalDateTime;

public interface Clock {

    LocalDateTime now();

    Instant instant();

    void setFixed(Instant fixed);
}