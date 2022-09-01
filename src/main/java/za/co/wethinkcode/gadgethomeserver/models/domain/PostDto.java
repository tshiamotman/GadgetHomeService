package za.co.wethinkcode.gadgethomeserver.models.domain;

import javax.xml.bind.annotation.XmlElement;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PostDto {
    @JsonProperty("model")
    private String model;

    @JsonProperty("brand")
    private String brand;

    @JsonProperty("device")
    private String device;

    @XmlElement(nillable = true)
    @JsonProperty("description")
    private String description;

    @JsonProperty("amount")
    private Double amount;

    public PostDto() {
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
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
