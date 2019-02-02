package model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Author: brianfroschauer
 * Date: 2018-12-26
 */
@Entity
@Table(name = "category")
public class Category {

    @Id
    @GeneratedValue
    @Column(name = "category_id")
    private Integer id;

    @Column(name = "category", unique = true, nullable = false)
    private String category;

    @OneToMany(mappedBy = "category")
    private List<Product> products;

    public Category(String category) {
        this.category = category;
    }

    public Category() {}

    public Integer getId() {
        return id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
