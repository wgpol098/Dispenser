using System.ComponentModel.DataAnnotations;

namespace WebApplication1.API.Resources
{
    public class DispenserResource
    {
        public string Login { get; set; }
        public int IdDispenser { get; set; }
        public string Name { get; set; }
    }
}