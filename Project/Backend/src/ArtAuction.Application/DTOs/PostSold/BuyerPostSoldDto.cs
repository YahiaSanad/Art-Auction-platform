namespace ArtAuction.Application.DTOs.PostSold;

public class BuyerPostSoldDto
{
    public int ArtworkPostId { get; set; }
    public string Title { get; set; }
    public decimal FinalPrice { get; set; }
    public bool IsPaid { get; set; }
}
