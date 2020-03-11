using System;

namespace WebApplication1.API.Domain.Models
{
    public class Historia
    {
        public int Id { get; set; }
        public int DispenserId { get; set; }
        public DateTime Datetime { get; set; }
        public int Nr_Okienka { get; set; }
        public string Opis { get; set; }
        public int Flaga { get; set; }
    }
}
