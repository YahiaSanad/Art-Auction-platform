using ArtAuction.Application.DTOs.ArtworkPost;
using ArtAuction.Application.DTOs.PostSold;
using ArtAuction.Application.Entities;
using ArtAuction.Application.Interfaces.Repositories;
using ArtAuction.Application.Interfaces.Services;
using AutoMapper;

namespace ArtAuction.Application.Services;

public class AuctionServices : IAuctionServices
{
    // Attributes
    private readonly IPostSoldRepo _postSoldRepo;
    private readonly IEmailServices _emailServices;
    private readonly INotificationServices _notificationServices;
    private readonly IMapper _mapper;
    private readonly IBuyerRepo _buyerRepo;
    private readonly IArtworkPostRepo _artworkPostRepo;
    private readonly IPostBidRepo _postBidRepo;
    
    // Constructor
    public AuctionServices(IPostSoldRepo postSoldRepo,  IEmailServices emailServices, 
        INotificationServices notificationServices, IMapper mapper, IBuyerRepo buyerRepo, 
        IArtworkPostRepo artworkPostRepo, IPostBidRepo postBidRepo)
    {
        _postSoldRepo = postSoldRepo;
        _emailServices = emailServices;
        _notificationServices = notificationServices;
        _mapper = mapper;
        _buyerRepo = buyerRepo;
        _artworkPostRepo = artworkPostRepo;
        _postBidRepo = postBidRepo;
    }
    
    // Methods
    public async Task DetermineWinner(int artworkPostId)
    {
        // Get buyer with the highest price
        var postSold = await GetWinner(artworkPostId);
        if (postSold == null)
            return;
        
        // Load approved post even if auction already ended
        var artworkPost = await _artworkPostRepo.GetArtworkPostForAuctionSettlement(artworkPostId);
        if (artworkPost == null)
            return;

        // Ensure auction is actually ended before creating winner record
        if (artworkPost.EndDate > DateTime.Now)
            return;

        if (!await _postSoldRepo.CreatePostSold(postSold))
            return;

        // Get winner details after sale record is created
        var buyer = await _buyerRepo.GetBuyerById(postSold.BuyerId);
        if (buyer == null)
            return;
        
        // Email and notification details
        var type = "AuctionEnded";
        var subject = "Auction Ended 🎉";
        var message = $"You won auction {artworkPost.Title}";
        var emailBody = CreateHtmlMessage(postSold, artworkPost, buyer);
        
        // Add notification to signalR
        await _notificationServices.SendToUserAsync(postSold.BuyerId.ToString(), new
        {
            type,
            subject,
            message
        });
        
        // Send email
        await _emailServices.SendEmail(buyer.Email, subject, emailBody);
    }

    private string CreateHtmlMessage(PostSold postSold, ArtworkPost artworkPost, Buyer buyer)
    {
        return $@"
            <!DOCTYPE html>
            <html>
            <body style='font-family:Arial; background:#f4f4f4; padding:20px;'>

            <div style='max-width:600px; margin:auto; background:white; padding:20px; border-radius:10px;'>
                <h1 style='color:#4CAF50;'>🎉 Congratulations {buyer.Name}!</h1>

                <p>You won the auction:</p>

                <h3>{artworkPost.Title}</h3>
                <p>Final Price: <strong>${postSold.FinalPrice}</strong></p>

                <a href='https://yourdomain.com/auctions/.Id' 
                   style='display:inline-block; padding:10px 20px; background:#4CAF50; color:white; text-decoration:none; border-radius:5px;'>
                   Click here to pay now
                </a>

                <p style='margin-top:20px;'>Thanks,<br/>ArtAuction Team</p>
            </div>

            </body>
            </html>";
    }

    private async Task<PostSold?> GetWinner(int artworkPostId)
    {
        // Get buyer with top price
        var postBid = await _postBidRepo.GetTopBid(artworkPostId);
        if (postBid == null)
            return null;
        
        // Return post sold 
        return new PostSold
        {
            ArtworkPostId = postBid.ArtworkPostId,
            BuyerId = postBid.BuyerId,
            FinalPrice = postBid.BuyerPrice
        };
    }
}
