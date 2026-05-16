package com.artauction.authenticationservices.Entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;

@Entity
@DiscriminatorValue("Artist")
@Setter
@Getter
public class Artist extends User {
    // Attributes
    @Column(length = 50)
    private String city;

    @Column(length = 50)
    private String country;

    @Column(length = 13)
    private String phoneNumber;
    private LocalDateTime hireDate;

    // Relationships
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "admin_id")
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    private Admin admin;
}