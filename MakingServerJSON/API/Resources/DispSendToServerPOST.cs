using System;
namespace WebApplication1.API.Resources
{
    public class DispSendToServerPOST
    {
        public int IdDispenser { get; set; }
        public DateTime DateAndTime { get; set; }
        public int NoWindow { get; set; }
        public int Flag { get; set; }
    }
}