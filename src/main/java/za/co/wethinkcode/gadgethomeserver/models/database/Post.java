package za.co.wethinkcode.gadgethomeserver.models.database;

import javax.persistence.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.annotation.CreatedDate;

import java.time.Instant;
import java.util.Date;

@Entity
@Table(name = "post")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "model")
    private String model;

    @Column(name = "brand")
    private String brand;

    @Column(name = "device")
    private String device;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;

    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_date")
    private Date createdDate;

    @Column(name = "description")
    private String description;

    @Column(name = "is_available")
    private boolean available;

    @Column(name = "price")
    private Double price;



    public Post(String device, String model, String brand, String description, User owner, Double price) {
        this.device = device;
        this.model = model;
        this.brand = brand;
        this.owner = owner;
        this.description = description;
        this.price = price;
        this.available = true;
        this.createdDate = Date.from(Instant.now());
    }

    public Post() {

    }

    public void setModel(String model) {
        this.model = model;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getModel() {
        return model;
    }

    public String getBrand() {
        return brand;
    }

    public User getOwner() {
        return owner;
    }

    public String getDevice() {
        return device;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return String.format("%s %s %s posted by %s on the %s", brand, model, device, owner.getUserName(), createdDate);
    }

    @Override
    public boolean equals(Object obj) {
        ObjectMapper objectMapper = new ObjectMapper();
        try{
            Post post = objectMapper.convertValue(obj, Post.class);
            return post.brand.equals(brand) && post.model.equals(model) && post.device.equals(device)
                    && owner.getUserName().equals(post.owner.getUserName()) && post.price.equals(price);
        } catch(Exception e) {
            return false;
        }
    }
}
