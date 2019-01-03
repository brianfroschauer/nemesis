package model;

import org.jetbrains.annotations.NotNull;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Author: brianfroschauer
 * Date: 2018-12-22
 */
@Entity
@Table(name = "sale")
public class Sale {

    @Id
    @GeneratedValue
    @Column(name = "sale_id")
    private Integer id;

    @OneToOne(targetEntity = Store.class)
    @JoinColumn(name = "store_id")
    private Store store;

    @OneToOne(targetEntity = User.class)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "SALE_PRODUCT",
            joinColumns = { @JoinColumn(name = "sale_id") },
            inverseJoinColumns = { @JoinColumn(name = "product_id") }
    )
    private List<Product> products = new ArrayList<>();

    public Sale(Store store, User user) {
        this.store = store;
        this.user = user;
    }

    public Sale() {}

    public Integer getId() {
        return id;
    }

    public Store getStore() {
        return store;
    }

    public void setStore(@NotNull Store store) {
        this.store = store;
    }

    public User getUser() {
        return user;
    }

    public void setUser(@NotNull User user) {
        this.user = user;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Sale sale = (Sale) o;
        return id.equals(sale.id) &&
                store.equals(sale.store) &&
                user.equals(sale.user) &&
                products.equals(sale.products);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, store, user, products);
    }
}
