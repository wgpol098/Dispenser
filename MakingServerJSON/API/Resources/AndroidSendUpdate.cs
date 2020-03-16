using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace WebApplication1.API.Resources
{
    public class AndroidSendUpdate
    {
        public int Id { get; set; }
        public int Hours { get; set; }
        public int Minutes { get; set; }
        public int Count { get; set; }
        public string Description { get; set; }
        public int Periodicity { get; set; }
    }
}
