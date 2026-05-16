package com.artauction.auctionservice.Email;

import com.artauction.auctionservice.Entities.PostSold;
import org.springframework.stereotype.Component;

@Component
public class EmailMessages {
    public String winnerHtmlMessage(String buyerName, String artworkTitle, PostSold postSold){
        return """
            <!DOCTYPE html>
            <html>
            <body style='font-family:Arial; background:#f4f4f4; padding:20px;'>
            <div style='max-width:600px; margin:auto; background:white; padding:20px; border-radius:10px;'>
                <h1 style='color:#4CAF50;'>🎉 Congratulations %s!</h1>
                <p>You won the auction:</p>
                <h3>%s</h3>
                <p>Final Price: <strong>$%s</strong></p>
                <a href='https://yourdomain.com/auctions/%d'
                   style='display:inline-block; padding:10px 20px; background:#4CAF50; color:white; text-decoration:none; border-radius:5px;'>
                   Click here to pay now
                </a>
                <p style='margin-top:20px;'>Thanks,<br/>ArtAuction Team</p>
            </div>
            </body>
            </html>
            """.formatted(
                buyerName,
                artworkTitle,
                postSold.getFinalPrice(),
                postSold.getId().getArtworkPostId()
        );
    }
}
