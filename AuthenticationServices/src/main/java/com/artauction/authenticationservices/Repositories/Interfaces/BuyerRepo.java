package com.artauction.authenticationservices.Repositories.Interfaces;

import com.artauction.authenticationservices.Entities.Buyer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BuyerRepo extends JpaRepository<Buyer, Integer> {
}
