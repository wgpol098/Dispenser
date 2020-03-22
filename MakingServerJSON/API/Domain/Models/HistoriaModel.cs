using System;
namespace WebApplication1.API.Domain.Models
{
    public class Historia
    {
        public int Id { get; set; }
        public int IdDispenser { get; set; }
        public DateTime DateAndTime { get; set; }
        public int NoWindow { get; set; }
        public string Description { get; set; }
        public int Flag { get; set; }
    }
}