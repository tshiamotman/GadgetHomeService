package za.co.wethinkcode.gadgethomeserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import za.co.wethinkcode.gadgethomeserver.models.database.Chat;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {
    Optional<Chat> findByMessageId(String messageId);

    List<Chat> findAllBySenderUserNameOrRecipientUserName(String senderUserName, String recipientUserName);
}
