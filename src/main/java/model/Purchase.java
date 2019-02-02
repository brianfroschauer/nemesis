package model;

import org.jetbrains.annotations.NotNull;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * Author: brianfroschauer
 * Date: 2018-12-22
 */
@Entity
@Table(name = "purchase")
public class Purchase {

    @Id
    @GeneratedValue
    @Column(name = "purchase_id")
    private Integer id;

    @OneToOne(targetEntity = Store.class)
    @JoinColumn(name = "store_id")
    private Store store;

    @ManyToOne(targetEntity = User.class)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToMany(targetEntity = Product.class)
    @JoinTable(
            name = "PURCHASE_PRODUCT",
            joinColumns = { @JoinColumn(name = "purchase_id") },
            inverseJoinColumns = { @JoinColumn(name = "product_id") }
    )
    private List<Product> products = new ArrayList<>();

    @Column(name = "amount")
    private double amount;

    @Temporal(TemporalType.DATE)
    @Column(name = "date")
    private Date date;

    public Purchase(Store store, User user, List<Product> products) {
        this.store = store;
        this.user = user;
        this.products = products;
    }

    public Purchase() {}

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

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Purchase purchase = (Purchase) o;
        return id.equals(purchase.id) &&
                store.equals(purchase.store) &&
                user.equals(purchase.user) &&
                products.equals(purchase.products);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, store, user, products);
    }
}
