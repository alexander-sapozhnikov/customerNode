package ru.sapozhnikov.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name="user_security")
public class UserSecurity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String username;
    private String password;
}
