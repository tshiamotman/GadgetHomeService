package za.co.wethinkcode.gadgethomeserver.repository;

import org.springframework.data.repository.CrudRepository;

import za.co.wethinkcode.gadgethomeserver.models.domain.Blacklist;

public interface BlacklistRepository extends CrudRepository<Blacklist, String> {
    
}
