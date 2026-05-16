package com.artauction.artworkpostservices.Seeder;

import com.artauction.artworkpostservices.Entities.*;
import com.artauction.artworkpostservices.Repositories.Interfaces.ArtworkPostRepo;
import com.artauction.artworkpostservices.Repositories.Interfaces.CategoryRepo;
import com.artauction.artworkpostservices.Repositories.Interfaces.TagRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ArtworkPostDataSeeder implements CommandLineRunner {
    // Attributes
    private final ArtworkPostRepo artworkPostRepo;
    private final CategoryRepo categoryRepo;
    private final TagRepo tagRepo;

    @Override
    public void run(String... args) throws Exception {
        /* if (artworkPostRepo.count() > 0) return;

        // Load image from local machine
        byte[] imageBytes;
        try {
            Path path = Paths.get("src/main/resources/artwork.jpg");
            imageBytes = Files.readAllBytes(path);
        } catch (Exception e) {
            imageBytes = "dummy image content".getBytes();
            System.out.println("Image not found at path, using dummy bytes.");
        }

        // 1. Seed 10 Categories
        List<Category> categories = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            Category c = new Category();
            c.setName("Category " + i);
            categories.add(categoryRepo.save(c));
        }

        // 2. Seed 10 Tags
        List<Tag> tags = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            Tag t = new Tag();
            t.setName("Tag " + i);
            tags.add(tagRepo.save(t));
        }

        // 3. Seed 10 Posts
        for (int i = 1; i <= 10; i++) {
            ArtworkPost post = new ArtworkPost();
            post.setTitle("Masterpiece #" + i);
            post.setDescription("Beautiful artwork description " + i);
            post.setInitialPrice(new BigDecimal("100.00").multiply(BigDecimal.valueOf(i)));
            post.setBuyNowPrice(post.getInitialPrice().multiply(BigDecimal.valueOf(2)));
            post.setStartDate(LocalDateTime.now());
            post.setEndDate(LocalDateTime.now().plusDays(7));
            post.setImage(imageBytes);
            post.setArtistId(3 + (i % 4)); // IDs of artists from AuthService
            post.setAdminId(1);
            post.setCategory(categories.get(i - 1));

            // 4. Assign 4 Tags to each post
            for (int j = 0; j < 4; j++) {
                PostTag pt = new PostTag();
                pt.setArtworkPost(post);
                pt.setTag(tags.get((i + j) % 10));
                post.getPostTags().add(pt);
            }

            // 5. Assign 3 WatchList for each buyer (assuming Buyer IDs are 7, 8, 9, 10)
            for (int b = 7; b <= 9; b++) {
                WatchList wl = new WatchList();
                wl.setArtworkPost(post);
                wl.getId().setBuyerId(b);
                post.getWatchLists().add(wl);
            }

            artworkPostRepo.save(post);
        }
        System.out.println(">> ArtworkService: Seeded Categories, Tags, and 10 Posts."); */
    }
}
