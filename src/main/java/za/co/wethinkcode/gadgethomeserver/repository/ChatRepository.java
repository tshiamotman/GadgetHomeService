package za.co.wethinkcode.gadgethomeserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import za.co.wethinkcode.gadgethomeserver.models.database.Chat;
import za.co.wethinkcode.gadgethomeserver.models.database.Post;
import za.co.wethinkcode.gadgethomeserver.models.database.User;

import java.util.Optional;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {
        Optional<Chat> findByMessageId(String messageId);
}
