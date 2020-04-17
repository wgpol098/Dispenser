using System.Collections.Generic;

namespace WebApplication1.API.Resources
{
    public class AndroidSendToAppByIdDispDoctorHistory
    {
        public string Description { get; set; }
        public string Start { get; set; }
        public string End { get; set; }
        public List<ListOfDate> TabDidnttake { get; set; }
        public string FirstHour { get; set; }
        public int Periodicity { get; set; }
        public int Count { get; set; }
    }
}