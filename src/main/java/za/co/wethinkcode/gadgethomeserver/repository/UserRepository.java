package za.co.wethinkcode.gadgethomeserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import za.co.wethinkcode.gadgethomeserver.models.User;

public interface UserRepository extends JpaRepository<User, String> {

    User findUserByUserName(String userName);

    boolean existsByUserName(String userName);
}
