using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace WebApplication1.API.Domain.Models
{
    public class Plan
    {
        public int Id { get; set; }
        public int DispenserId { get; set; }
        public DateTime dateTime { get; set; }
        public int Nr_Okienka { get; set; }
        public string Opis{ get; set; }
    }
}
