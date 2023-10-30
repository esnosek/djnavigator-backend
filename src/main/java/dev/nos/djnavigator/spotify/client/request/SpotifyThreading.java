package dev.nos.djnavigator.spotify.client.request;

import java.util.concurrent.*;
import java.util.function.Supplier;

public class SpotifyThreading {

    private static final ExecutorService THREAD_POOL = executorService();

    public static <T> CompletableFuture<T> executeAsync(Supplier<T> callable) {
        return CompletableFuture.supplyAsync(
                callable, THREAD_POOL
        );
    }

    private static ThreadPoolExecutor executorService() {
        return new ThreadPoolExecutor(
                4,
                8,
                0L,
                TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(100)
        );
    }
}
