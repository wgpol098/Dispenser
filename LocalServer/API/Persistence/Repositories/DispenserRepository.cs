using Microsoft.EntityFrameworkCore;
using System.Collections.Generic;
using System.Threading.Tasks;
using WebApplication1.API.Domain.Models;
using WebApplication1.API.Domain.Repositories;
using WebApplication1.API.Persistence.Contexts;

namespace WebApplication1.API.Persistence.Repositories
{
    public class DispenserRepository : BaseRepository, IDispenserRepository
    {
        public DispenserRepository(AppDbContext context) : base(context)
        {
        }

        public async Task<IEnumerable<Dispenser>> ListAsync()
        {
            return await _context.Dispensers.ToListAsync();
        }
    }
}
