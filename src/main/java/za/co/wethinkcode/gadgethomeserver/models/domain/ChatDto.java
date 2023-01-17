package za.co.wethinkcode.gadgethomeserver.models.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Date;

@JsonIgnoreProperties
public class ChatDto {
    private Long id;

    private String message;

    private String messageId;

    private String senderUsername;

    private UserDto sender;

    private String recipientUsername;

    private UserDto recipient;

    private Date createdDate;

    private Date messageDelivered;

    private Date messageRead;

    private PostDto post;

    public ChatDto() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getSenderUsername() {
        return senderUsername;
    }

    public void setSenderUsername(String senderUsername) {
        this.senderUsername = senderUsername;
    }

    public UserDto getSender() {
        return sender;
    }

    public void setSender(UserDto sender) {
        this.sender = sender;
    }

    public String getRecipientUsername() {
        return recipientUsername;
    }

    public void setRecipientUsername(String recipientUsername) {
        this.recipientUsername = recipientUsername;
    }

    public UserDto getRecipient() {
        return recipient;
    }

    public void setRecipient(UserDto recipient) {
        this.recipient = recipient;
    }

    public Date getMessageDelivered() {
        return messageDelivered;
    }

    public void setMessageDelivered(Date messageDelivered) {
        this.messageDelivered = messageDelivered;
    }

    public Date getMessageRead() {
        return messageRead;
    }

    public void setMessageRead(Date messageRead) {
        this.messageRead = messageRead;
    }

    public PostDto getPost() {
        return post;
    }

    public void setPost(PostDto post) {
        this.post = post;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }
}
