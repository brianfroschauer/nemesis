package model;

import org.jetbrains.annotations.NotNull;

import javax.persistence.*;
import java.util.Objects;

/**
 * Author: brianfroschauer
 * Date: 28/04/2018
 */
@Entity
@Table(name = "address")
public class Address {

    @Id
    @GeneratedValue
    @Column(name = "address_id")
    private Integer id;

    @OneToOne(targetEntity = State.class)
    @JoinColumn(name = "state_id", nullable = false)
    private State state;

    @Column(name = "address", nullable = false)
    private String address;

    public Address(State state, String address) {
        this.state = state;
        this.address = address;
    }

    public Address() {}

    public Integer getId() {
        return id;
    }

    public State getState() {
        return state;
    }

    public void setState(@NotNull State state) {
        this.state = state;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Address address1 = (Address) o;
        return id.equals(address1.id) &&
                address.equals(address1.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, address);
    }
}