namespace WebApplication1.API.Resources
{
    public class AndroidSendPost
    {
        public int Hour { get; set; }
        public int Minutes { get; set; }
        public int Count { get; set; }
        public int Days { get; set; }
        public int Periodicity { get; set; }
        public string Description { get; set; }
        public int IdDispenser { get; set; } //Token
    }
}