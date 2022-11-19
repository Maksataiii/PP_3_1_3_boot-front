package ru.kata.spring.boot_security.demo.model;


import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@Table(name = "roles")
public class Role implements GrantedAuthority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String authority;
    @ManyToMany(mappedBy = "roles")
    private List< User > users;

    public Role() {
    }
    public Role(String role) {
    if (!role.startsWith("ROLE_")) {
        role = "ROLE_" + role;
    }
    setAuthority(role);
    }
}
