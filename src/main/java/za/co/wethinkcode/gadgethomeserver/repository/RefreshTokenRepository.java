package za.co.wethinkcode.gadgethomeserver.repository;

import java.util.Optional;

import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import za.co.wethinkcode.gadgethomeserver.models.domain.RefreshToken;

@Repository
public class RefreshTokenRepository{

    private final HashOperations hashOperations;

    public RefreshTokenRepository(RedisTemplate redisTemplate) {
        this.hashOperations = redisTemplate.opsForHash();
    }

    public void save(RefreshToken refreshToken) {
        this.hashOperations.put("refreshtoken", refreshToken.getToken(), refreshToken);
    }

    public Optional<RefreshToken> findById(String id) {
        return (Optional<RefreshToken>) this.hashOperations.get("refreshtoken", id);
    }

    public void delete(RefreshToken refreshToken) {
        hashOperations.delete("RefreshToken", refreshToken.getToken());
    }

    // Optional<RefreshToken> findByUser(String userName);

    // Optional<RefreshToken> findByToken(String token);
    
}
