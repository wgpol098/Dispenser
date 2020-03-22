using Microsoft.EntityFrameworkCore;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using WebApplication1.API.Domain.Models;
using WebApplication1.API.Domain.Repositories;
using WebApplication1.API.Persistence.Contexts;
using WebApplication1.API.Resources;

namespace WebApplication1.API.Persistence.Repositories
{
    public class AccountRepository : BaseRepository, IAccountRepository
    {
        public AccountRepository(AppDbContext context) : base(context)
        {
        }

        public async Task AddAsync(Account account)
        {
            await _context.Accounts.AddAsync(account);
        }

        public async Task<bool> CheckIfAccountIsInDatabase(Account account)
        {
            var acc = await _context.Accounts.FirstOrDefaultAsync(q => q.Login == account.Login);
            if (acc == null)
                return false;
            else return true;
        }

        public async Task<Account> FindByAccountAsync(SentAccount sentAccount)
        {
            return await _context.Accounts.FirstOrDefaultAsync(q => q.Login == sentAccount.Login 
                && q.Password == sentAccount.Password);
        }

        public async Task<bool> Remove(Account account)
        {
            var temp = await _context.ListOfDispensers.FirstOrDefaultAsync(q => q.IdAccount == account.Id);

            if (account == null)
                return false;

            if (temp != null)
                _context.ListOfDispensers.Remove(temp);

            _context.Accounts.Remove(account);

            try
            {
                await _context.SaveChangesAsync();
            }
            catch (Exception)
            {
                return false;
            }
            return true;
        }

        public async Task<IEnumerable<ListOfDispenser>> GetDispensersByLoginAndPassword(string login, string password)
        {
            Account acc = await _context.Accounts.FirstOrDefaultAsync(q => q.Login == login);

            if (acc == null || acc.Password != password)
            {
                List<ListOfDispenser> temp = new List<ListOfDispenser>()
                    { new ListOfDispenser(){ Id = -1, IdDispenser = -1, IdAccount = -1 } };
                return temp; 
            }

            return await _context.ListOfDispensers.Where(q => q.IdAccount == acc.Id).ToListAsync();
        }

        public async Task<Account> GetTypeOfAccount(string login)
        {
            return await _context.Accounts.FirstOrDefaultAsync(q => q.Login == login);
        }
    }
}