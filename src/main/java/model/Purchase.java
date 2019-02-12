package model;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.*;

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

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToMany
    @JoinTable(
            name = "PURCHASE_ITEM",
            joinColumns = { @JoinColumn(name = "purchase_id") },
            inverseJoinColumns = { @JoinColumn(name = "item_id") }
    )
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Item> items = new ArrayList<>();

    @Column(name = "amount")
    private Integer amount;

    @Temporal(TemporalType.DATE)
    @Column(name = "date")
    private Date date;

    public Purchase(User user, List<Item> items, Integer amount) {
        this.user = user;
        this.items = items;
        this.amount = amount;
        this.date = new Date();
    }

    public Purchase() {}

    public Integer getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
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
                user.equals(purchase.user) &&
                items.equals(purchase.items);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user, items);
    }
}
