package za.co.wethinkcode.gadgethomeserver.repository;

import java.util.Optional;

import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import za.co.wethinkcode.gadgethomeserver.models.domain.Blacklist;

@Repository
public class BlacklistRepository {
    private final HashOperations hashOperations;

    public BlacklistRepository(RedisTemplate redisTemplate) {
        this.hashOperations = redisTemplate.opsForHash();
    }

    public void save(Blacklist blacklist) {
        this.hashOperations.put("blacklist", blacklist.getUsername(), blacklist);
    }

    public Optional<Blacklist> findById(String id) {
        return (Optional<Blacklist>) this.hashOperations.get("blacklist", id);
    }

    public void delete(String id) {
        hashOperations.delete("blacklist", id);
    }
}
