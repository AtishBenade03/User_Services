package com.UserService.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
@Table(name = "users")
public class User  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstname;
    private String lastname;

    @Column(name = "mobile",unique = true) // Specifies a column in the database with unique constraint
    private String mobile;

    @Column(name = "email",unique = true) // Specifies a column in the database with unique constraint
    private String email;
    private String password;
    private String dateOfBirth;
    private String role;


    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL) // Defines a one-to-one relationship with Address entity
    private Address address;


   
}
