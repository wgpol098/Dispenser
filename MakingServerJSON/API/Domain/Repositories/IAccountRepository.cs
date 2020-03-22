using System.Collections.Generic;
using System.Threading.Tasks;
using WebApplication1.API.Domain.Models;
using WebApplication1.API.Resources;

namespace WebApplication1.API.Domain.Repositories
{
    public interface IAccountRepository
    {
        Task<IEnumerable<ListOfDispenser>> GetDispensersByLoginAndPassword(string login, string password);
        Task AddAsync(Account account);
        Task<Account> FindByAccountAsync (SentAccount sentAccount);
        Task<bool> CheckIfAccountIsInDatabase (Account account);
        Task<bool> Remove(Account account);
        Task<Account> GetTypeOfAccount(string login);
    }
}