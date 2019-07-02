package com.example.oauth.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "app_role")
@Data
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @Column(name = "description")
    private String description;

    @Column(name = "role_name")
    private String roleName;

}
