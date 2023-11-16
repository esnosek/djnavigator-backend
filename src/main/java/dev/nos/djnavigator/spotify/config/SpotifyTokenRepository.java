package dev.nos.djnavigator.spotify.config;

import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static java.util.Optional.ofNullable;

@Service
public class SpotifyTokenRepository {

    private final RedisTemplate<String, SpotifyToken> redisTemplate;

    @Autowired
    public SpotifyTokenRepository(RedisTemplate<String, SpotifyToken> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public Optional<SpotifyToken> find(String key) {
        return ofNullable(get(key));
    }

    public SpotifyToken get(String key) {
        return redisTemplate
                .opsForValue()
                .get(key);
    }

    public void delete(String key) {
        redisTemplate.delete(key);
    }

    public void set(String key, SpotifyToken value) {
        redisTemplate.execute(new SessionCallback<>() {
            @SuppressWarnings("unchecked")
            public Object execute(@NonNull RedisOperations operations) throws DataAccessException {
                operations.watch(key);
                operations.multi();
                operations.opsForValue().set(key, value);
                return operations.exec();
            }
        });
    }
}
