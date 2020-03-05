using Microsoft.EntityFrameworkCore;
using System;
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

        public async Task<Dispenser> GetByIdAsync(int id)
        {
            return await _context.Dispensers.FirstOrDefaultAsync(x => x.Id == id);
        }

        public async Task<Dispenser> GetByLoginAsync(string login)
        {
            return await _context.Dispensers.FirstOrDefaultAsync(x => x.Login == login);
        }

        public async Task<IEnumerable<Dispenser>> ListAsync()
        {
            return await _context.Dispensers.ToListAsync();
        }

        public async Task AddAsync(Dispenser dispenser)
        {
            await _context.Dispensers.AddAsync(dispenser);
        }

        public async Task<Dispenser> FindByIdAsync(int id)
        {
            return await _context.Dispensers.FindAsync(id);
        }

        public void Update(Dispenser dispenser)
        {
            _context.Dispensers.Update(dispenser);
        }

        public async Task<bool> Remove(Dispenser dispenser)
        {
            if (dispenser == null)
                return false;

            _context.Dispensers.Remove(dispenser);

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
    }
}