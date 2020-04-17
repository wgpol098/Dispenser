using System.Collections.Generic;

namespace WebApplication1.API.Resources
{
    public class AndroidSendToAppByIdDispDoctorPlan
    {
        public string Description { get; set; }
        public string Start { get; set; }
        public int DaysLeft { get; set; }
        public string FirstHour { get; set; }
        public int Periodicity { get; set; }
        public List<ListOfDate> TabDidnttake { get; set; }
    }
}