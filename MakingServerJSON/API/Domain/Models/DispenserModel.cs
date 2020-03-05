namespace WebApplication1.API.Domain.Models
{
    public class Dispenser
    {
        public int Id { get; set; }
        public string Name { get; set; }
        public string Login { get; set; }
        public string Password { get; set; }
        public bool TurnDiode { get; set; }
    }
}