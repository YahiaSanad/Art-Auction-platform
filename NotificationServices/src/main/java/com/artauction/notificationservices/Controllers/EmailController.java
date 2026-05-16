package com.artauction.notificationservices.Controllers;

import com.artauction.notificationservices.Dtos.EmailDto;
import com.artauction.notificationservices.Services.Interfaces.EmailServices;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/Email")
@RequiredArgsConstructor
public class EmailController {
    // Attributes
    private final EmailServices emailServices;

    // Send email API
    @PostMapping("/SendEmail")
    public ResponseEntity<String> sendEmail(@Valid @RequestBody EmailDto emailDto){
        // Check if service Sent the email
        String message = emailServices.sendEmail(emailDto);

        // Return response
        return ResponseEntity.ok(message);
    }
}
