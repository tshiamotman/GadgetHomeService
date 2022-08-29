package za.co.wethinkcode.gadgethomeserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import za.co.wethinkcode.gadgethomeserver.models.database.User;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    User findUserByUserName(String userName);

    boolean existsByUserName(String userName);

    boolean existsByEmail(String email);
}
