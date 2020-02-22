using System.ComponentModel.DataAnnotations;

namespace WebApplication1.API.Resources
{
    public class SaveDispenserResource
    {
        [Required]
        [MaxLength(30)]
        public string Name { get; set; }
    }
}