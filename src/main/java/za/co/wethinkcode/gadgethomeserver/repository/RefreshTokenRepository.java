package za.co.wethinkcode.gadgethomeserver.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;
import org.springframework.stereotype.Repository;

import za.co.wethinkcode.gadgethomeserver.models.domain.RefreshToken;

@Repository
public interface RefreshTokenRepository extends CrudRepository<RefreshToken, String>, QueryByExampleExecutor<RefreshToken>{

    Optional<RefreshToken> findByUser(String userName);

    Optional<RefreshToken> findByToken(String token);
    
}
