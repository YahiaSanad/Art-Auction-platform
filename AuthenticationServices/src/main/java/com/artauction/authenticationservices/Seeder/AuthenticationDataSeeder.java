package com.artauction.authenticationservices.Seeder;

import com.artauction.authenticationservices.Entities.Admin;
import com.artauction.authenticationservices.Entities.Artist;
import com.artauction.authenticationservices.Entities.Buyer;
import com.artauction.authenticationservices.Repositories.Interfaces.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class AuthenticationDataSeeder implements CommandLineRunner {
    // Attributes
    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
       /* if (userRepo.count() > 0) return;

        String pass = passwordEncoder.encode("Password123");

        // 1. Create 2 Admins
        Admin admin1 = createAdmin("David Admin", "admin1@art.com", pass);
        Admin admin2 = createAdmin("Sarah Admin", "admin2@art.com", pass);
        userRepo.save(admin1);
        userRepo.save(admin2);

        // 2. Create 4 Artists (Total users counting up)
        for (int i = 1; i <= 4; i++) {
            Artist artist = new Artist();
            artist.setName("Artist " + i);
            artist.setEmail("artist" + i + "@art.com");
            artist.setPassword(pass);
            artist.setCity("Cairo");
            artist.setCountry("Egypt");
            artist.setHireDate(LocalDateTime.now());
            artist.setAdmin(admin1);
            userRepo.save(artist);
        }

        // 3. Create 4 Buyers (Total 10 users)
        for (int i = 1; i <= 4; i++) {
            Buyer buyer = new Buyer();
            buyer.setName("Buyer " + i);
            buyer.setEmail("buyer" + i + "@mail.com");
            buyer.setPassword(pass);
            buyer.setCity("Alexandria");
            buyer.setAddress("Street " + i);
            userRepo.save(buyer);
        }
        System.out.println(">> AuthService: Seeded 2 Admins, 4 Artists, 4 Buyers.");
    }

    private Admin createAdmin(String name, String email, String pass) {
        Admin admin = new Admin();
        admin.setName(name);
        admin.setEmail(email);
        admin.setPassword(pass);
        return admin; */
    }
}
