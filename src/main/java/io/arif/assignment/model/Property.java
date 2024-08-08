package io.arif.assignment.model;

import jakarta.persistence.*;

@Entity
public class Property {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String name;
    private Float price;
    private String description;

    public Property() {
    }

    public Property(Integer id, String name, Float price, String description) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.description = description;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Property{" +
                "name='" + name + '\'' +
                ", price=" + price +
                ", description='" + description + '\'' +
                '}';
    }
}
