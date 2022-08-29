package za.co.wethinkcode.gadgethomeserver.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import za.co.wethinkcode.gadgethomeserver.models.domain.Blacklist;

@Repository
public interface BlacklistRepository extends CrudRepository<Blacklist, String>{

    Optional<Blacklist> findByUsername(String username);
}
