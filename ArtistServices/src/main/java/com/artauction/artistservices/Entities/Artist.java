package com.artauction.artistservices.Entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "role", discriminatorType = DiscriminatorType.STRING)
@DiscriminatorValue("Artist")
@Where(clause = "role = 'Artist'")
@Getter
@Setter
public class Artist {
    // Attributes
    @Id
    private Integer id;
    private String name;
    private String email;
    private String password;
    private String city;
    private String country;
    private LocalDateTime hireDate;
    private String phoneNumber;

    // Relationships
    @Column(name = "admin_id")
    private Integer adminId;
}
