package za.co.wethinkcode.gadgethomeserver.models.database;

import javax.persistence.Entity;
import javax.persistence.*;

@Entity(name = "chat")
public class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String message;

    private String messageId;

    @ManyToOne
    @JoinColumn(name = "sender_id")
    private User sender;

    @ManyToOne
    @JoinColumn(name = "recipient_id")
    private User recipient;

    private Boolean messageDelivered;

    private Boolean messageRead;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    public Chat() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User buyer) {
        this.sender = buyer;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public User getRecipient() {
        return recipient;
    }

    public void setRecipient(User recipient) {
        this.recipient = recipient;
    }

    public Boolean getMessageDelivered() {
        return messageDelivered;
    }

    public void setMessageDelivered(Boolean messageDelivered) {
        this.messageDelivered = messageDelivered;
    }

    public Boolean getMessageRead() {
        return messageRead;
    }

    public void setMessageRead(Boolean messageRead) {
        this.messageRead = messageRead;
    }
}
