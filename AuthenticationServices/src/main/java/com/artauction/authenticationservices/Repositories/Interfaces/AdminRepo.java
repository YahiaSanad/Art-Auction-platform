package com.artauction.authenticationservices.Repositories.Interfaces;

import com.artauction.authenticationservices.Entities.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepo extends JpaRepository<Admin, Integer> {
}
