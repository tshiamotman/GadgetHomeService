package za.co.wethinkcode.gadgethomeserver.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.time.LocalDate;

@Entity
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String model;
    private String brand;
    private String device;

    @ManyToOne
    @JoinColumn(name = "owner_user_name")
    private User owner;
    private LocalDate datePosted;

    private String description;
    private boolean available;
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
}
