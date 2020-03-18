using Microsoft.EntityFrameworkCore;
using System.Collections.Generic;
using System.Threading.Tasks;
using WebApplication1.API.Domain.Models;
using WebApplication1.API.Domain.Repositories;
using WebApplication1.API.Persistence.Contexts;

namespace WebApplication1.API.Persistence.Repositories
{
    public class DebugRepository: BaseRepository, IDebugRepository
    {
        public DebugRepository(AppDbContext context) : base(context)
        {
        }

        public async Task<IEnumerable<Plan>> FindAllRecordsInPlans()
        {
            return await _context.Plans.ToListAsync();
        }

        public async Task<IEnumerable<Historia>> FindAllRecordsInHistory()
        {
            return await _context.History.ToListAsync();
        }

        public async Task<IEnumerable<Account>> FindAllRecordsInAccounts()
        {
            return await _context.Accounts.ToListAsync();
        }

        public async Task<IEnumerable<Dispenser>> FindAllRecordsInDispensers()
        {
            return await _context.Dispensers.ToListAsync();
        }
    }
}