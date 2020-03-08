namespace WebApplication1.API.Domain.Models
{
    public class Account
    {
        public int Id { get; set; }
        public string Login { get; set; }
        public string Password { get; set; }

        //Temp 1 Dispenser
        public Dispenser Dispensers { get; set; }
        public int DispenserId { get; set; }
        //To do later:
        //public IList<Dispenser> Dispensers { get; set; } = new List<Dispenser>();
    }
}
