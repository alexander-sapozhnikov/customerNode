package ru.sapozhnikov.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
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
    @JoinTable(name = "customerspaidtypes",
            joinColumns=@JoinColumn(name="customersid"),
            inverseJoinColumns=@JoinColumn(name="paidtypeid"))
    private List<PaidType> paidTypeList = new ArrayList<>();

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "addressid", referencedColumnName = "id")
    private Address address;


}
