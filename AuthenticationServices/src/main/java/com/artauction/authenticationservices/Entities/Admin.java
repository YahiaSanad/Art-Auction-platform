package com.artauction.authenticationservices.Entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@DiscriminatorValue("Admin")
@Setter
@Getter
public class Admin extends User {
    // Relationships
    @OneToMany(mappedBy = "admin")
    private List<Artist> artists = new ArrayList<Artist>();
}
