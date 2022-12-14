package com.springboot.blog.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "users", uniqueConstraints = {@UniqueConstraint(columnNames = {"username", "email"})})
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String username;
    private String email;
    private String password;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "user_roles", // nombre de la tabla temporal que tiene las PK de ambas entidades
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"), // referencia al id de User
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id")) // referencia al id de Role
    private Set<Role> roles;
}
