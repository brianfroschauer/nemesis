package model;

import javax.persistence.*;
import java.util.Objects;

/**
 * Author: brianfroschauer
 * Date: 2018-12-26
 */
@Entity
@Table(name = "country")
public class Country {

    @Id
    @GeneratedValue
    @Column(name = "country_id")
    private Integer id;

    @Column(name = "country", unique = true, nullable = false)
    private String country;

    public Country(String country) {
        this.country = country;
    }

    public Country() {}

    public Integer getId() {
        return id;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Country country1 = (Country) o;
        return id.equals(country1.id) &&
                country.equals(country1.country);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, country);
    }
}