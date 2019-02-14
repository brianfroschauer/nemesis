package model;

import org.jetbrains.annotations.NotNull;

import javax.persistence.*;

/**
 * Author: brianfroschauer
 * Date: 2018-12-26
 */
@Entity
@Table(name = "comment")
public class Comment {

    @Id
    @GeneratedValue
    @Column(name = "comment_id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(name = "comment")
    private String comment;

    public Comment(User user, Product product, String comment) {
        this.user = user;
        this.product = product;
        this.comment = comment;
    }

    public Comment() {}

    public Integer getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(@NotNull User user) {
        this.user = user;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(@NotNull Product product) {
        this.product = product;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}