package za.co.wethinkcode.gadgethomeserver.models.database;

import javax.persistence.*;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDate;

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
    @JoinColumn(name = "owner_user_name")
    private User owner;

    @Column(name = "date_posted")
    private LocalDate datePosted;

    @Column(name = "description")
    private String description;

    @Column(name = "available")
    private boolean available;

    @Column(name = "amount")
    private Double amount;

    public Post(String device, String model, String brand, String description, User owner, Double amount) {
        this.device = device;
        this.model = model;
        this.brand = brand;
        this.owner = owner;
        this.description = description;
        this.amount = amount;
        this.available = true;
        this.datePosted = LocalDate.now();
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

    public void setDatePosted(LocalDate datePosted) {
        this.datePosted = datePosted;
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

    public LocalDate getDatePosted() {
        return datePosted;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return String.format("%s %s %s posted by %s on the %s", brand, model, device, owner.getUserName(), datePosted);
    }

    @Override
    public boolean equals(Object obj) {
        ObjectMapper objectMapper = new ObjectMapper();
        try{
            Post post = objectMapper.convertValue(obj, Post.class);
            return post.brand.equals(brand) && post.model.equals(model) && post.device.equals(device)
                    && owner.getUserName().equals(post.owner.getUserName());
        } catch(Exception e) {
            return false;
        }
    }
}
