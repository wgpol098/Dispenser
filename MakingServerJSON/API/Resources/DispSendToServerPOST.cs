using System;
namespace WebApplication1.API.Resources
{
    public class DispSendToServerPOST
    {
        public int DispenserId { get; set; }
        public DateTime DateAndTime { get; set; }
        public int Nr_Okienka { get; set; }
        public int Flaga { get; set; }
    }
}