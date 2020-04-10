package ru.sapozhnikov.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name="paidType")
public class PaidType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    @ManyToMany
    @JoinTable(name = "customersPaidTypes",
            joinColumns=@JoinColumn(name="paidTypeId"),
            inverseJoinColumns=@JoinColumn(name="customersId"))
    private List<Customer> customers;
}
