using System;

namespace WebApplication1.API.Domain.Models
{
    public class Plan
    {
        public int Id { get; set; }
        public int IdDispenser { get; set; }
        public DateTime DateAndTime { get; set; }
        public int NoWindow { get; set; }
        public string Description { get; set; }
    }
}