namespace WebApplication1.API.Domain.Models
{
    public class Account
	{
		public int Id { get; set; }         //IdKonta w ListOfDispensers
		public string Login { get; set; }
		public string Password { get; set; }
		public string Name { get; set; }        //Opcjonalnie
		public int TypeAccount { get; set; }
	}
}