package util;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Author: brianfroschauer
 * Date: 2019-02-17
 */
public class PaymentData {

    private final String customer;
    private final Integer dni;
    private final String address;

    @JsonCreator
    public PaymentData(@JsonProperty("customer") String customer,
                       @JsonProperty("dni") Integer dni,
                       @JsonProperty("address") String address) {
        this.customer = customer;
        this.dni = dni;
        this.address = address;
    }

    public String getCustomer() {
        return customer;
    }

    public Integer getDni() {
        return dni;
    }

    public String getAddress() {
        return address;
    }
}
