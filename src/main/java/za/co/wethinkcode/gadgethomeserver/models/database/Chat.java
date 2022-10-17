package za.co.wethinkcode.gadgethomeserver.models.database;

import javax.persistence.Entity;
import javax.persistence.*;

@Entity
public class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String queueName;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne
    @JoinColumn(name = "buyer_id")
    private User buyer;

    public Chat(Post post, User buyer) {
        this.post = post;
        this.buyer = buyer;

        this.queueName = String.format("%s_%s_%s", buyer.getUserName(), post.getOwner().getUserName(), post.getId());
    }

    public Chat() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getQueueName() {
        return queueName;
    }

    public void setQueueName(String queueName) {
        this.queueName = queueName;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public User getBuyer() {
        return buyer;
    }

    public void setBuyer(User buyer) {
        this.buyer = buyer;
    } 
}
