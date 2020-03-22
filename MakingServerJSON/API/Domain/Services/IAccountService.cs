using System.Collections.Generic;
using System.Threading.Tasks;
using WebApplication1.API.Domain.Models;
using WebApplication1.API.Domain.Services.Communication;
using WebApplication1.API.Resources;

namespace WebApplication1.API.Domain.Repositories
{
    public interface IAccountService
    {
        Task<IEnumerable<ListOfDispenser>> GetDispensersByLoginAndPassword(string login, string password);
        Task<AccountResponse> SaveAsync(Account account);
        Task<AccountResponse> DeleteAsync(SentAccount sentAccount);
        Task<Account> CheckAccountByPass(string login);
    }
}