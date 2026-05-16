package com.artauction.notificationservices.Services.Implementations;

import com.artauction.notificationservices.Dtos.EmailDto;
import com.artauction.notificationservices.Services.Interfaces.EmailServices;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.MessagingException;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailServicesImp implements EmailServices {
    // Attributes
    private final JavaMailSender mailSender;

    @Override
    public String sendEmail(EmailDto emailDto) {
        try {
            // Create message container and helper
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            // Set sender and email properties
            helper.setFrom("divademadpro160@gmail.com");
            helper.setTo(emailDto.getEmail());
            helper.setSubject(emailDto.getSubject());
            helper.setText(emailDto.getBody(), true);

            // Send email
            mailSender.send(message);

            // return success message
            return "Email sent successfully to " + emailDto.getEmail();
        } catch (MessagingException messagingException) {
            return "Error: cannot send email";
        }
    }
}
