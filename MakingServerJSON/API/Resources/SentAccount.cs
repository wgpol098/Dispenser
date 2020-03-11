using System.ComponentModel.DataAnnotations;

namespace WebApplication1.API.Resources
{
    public class SentAccount
    {
        [Required]
        public string Login { get; set; }
        [Required]
        public string Password { get; set; }
    }
}
