package model;

import org.jetbrains.annotations.NotNull;

import javax.persistence.*;
import java.util.Objects;

/**
 * Author: brianfroschauer
 * Date: 2018-12-26
 */
@Entity
@Table(name = "state")
public class State {

    @Id
    @GeneratedValue
    @Column(name = "state_id")
    private Integer id;

    @OneToOne(targetEntity = Country.class)
    @JoinColumn(name = "country_id", nullable = false)
    private Country country;

    @Column(name = "state", unique = true, nullable = false)
    private String state;

    public State(Country country, String state) {
        this.country = country;
        this.state = state;
    }

    public State() {}

    public Integer getId() {
        return id;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(@NotNull Country country) {
        this.country = country;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        State state1 = (State) o;
        return id.equals(state1.id) &&
                state.equals(state1.state);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, state);
    }
}