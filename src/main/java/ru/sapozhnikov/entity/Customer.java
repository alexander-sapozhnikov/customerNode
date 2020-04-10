package ru.sapozhnikov.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "customers")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "firstname")
    private String firstName;
    @Column(name = "lastname")
    private String lastName;
    private String email;
    private String password;
    private String phone;

    @ManyToMany
    @JoinTable(name = "customersPaidTypes",
            joinColumns=@JoinColumn(name="customersId"),
            inverseJoinColumns=@JoinColumn(name="paidTypeId"))
    private List<PaidType> paidTypeList;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "addressid", referencedColumnName = "id")
    private Address address;

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", phone='" + phone + '\'' +
                ", paidTypeList=" + paidTypeList +
                ", address=" + address +
                '}';
    }
}
