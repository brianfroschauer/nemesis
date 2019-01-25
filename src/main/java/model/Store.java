package model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.jetbrains.annotations.NotNull;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Author: brianfroschauer
 * Date: 29/04/2018
 */
@Entity
@Table(name = "store")
public class Store {

    @Id
    @GeneratedValue
    @Column(name = "store_id")
    private Integer id;

    @Column(name = "name", unique = true, nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "image")
    private String image;

    @ManyToMany(mappedBy = "stores", cascade = CascadeType.REMOVE)
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<User> users = new ArrayList<>();

    @OneToMany(mappedBy = "store", cascade = CascadeType.REMOVE)
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Product> products = new ArrayList<>();

    public Store(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public Store() {}

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @JsonIgnore
    public List<User> getUsers() {
        return users;
    }

    public void addUser(@NotNull User user) {
        users.add(user);
    }

    public void removeUser(@NotNull User user) {
        users.remove(user);
    }

    public List<Product> getProducts() {
        return products;
    }

    public void addProduct(@NotNull Product product) {
        products.add(product);
        product.setStore(this);
    }

    public void removeProduct(@NotNull Product product) {
        products.remove(product);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Store store = (Store) o;
        return id.equals(store.id) &&
                name.equals(store.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}