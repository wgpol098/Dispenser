using Microsoft.EntityFrameworkCore;
using System;
using System.Threading.Tasks;
using WebApplication1.API.Domain.Models;
using WebApplication1.API.Domain.Repositories;
using WebApplication1.API.Persistence.Contexts;
using WebApplication1.API.Resources;

namespace WebApplication1.API.Persistence.Repositories
{
    public class DispenserRepository : BaseRepository, IDispenserRepository
    {
        public DispenserRepository(AppDbContext context) : base(context)
        {
        }

        public async Task<ListOfDispenser> AddAsync(DispenserResource dispenser)
        {
            var temp = await _context.Dispensers.FirstOrDefaultAsync(q => q.IdDispenser == dispenser.IdDispenser);
            var acc = await _context.Accounts.FirstOrDefaultAsync(q => q.Login == dispenser.Login);

            ListOfDispenser listOfDispenser = new ListOfDispenser()
            {
                IdAccount = acc.Id,
                IdDispenser = dispenser.IdDispenser,
                Name = dispenser.Name
            };

            _context.ListOfDispensers.Add(listOfDispenser);

            if (temp == null)
            {
                Dispenser disp = new Dispenser()
                {
                    IdDispenser = dispenser.IdDispenser,
                    NoWindow = "0000000",
                    NoUpdate = 0
                };

                await _context.Dispensers.AddAsync(disp);

                return listOfDispenser;
            } 
            else 
                return listOfDispenser;
        }

        public async Task<ListOfDispenser> FindByLoginAndIdAsync(DispenserResource dispenserResource)
        {
            var acc = await _context.Accounts.FirstOrDefaultAsync(q => q.Login == dispenserResource.Login);
            var temp = await _context.ListOfDispensers.FirstOrDefaultAsync(q => q.IdDispenser == dispenserResource.IdDispenser && q.IdAccount == acc.Id);
            return temp;
        }

        public async Task<bool> Remove(ListOfDispenser dispenser)
        {
            if (dispenser == null)
                return false;

            _context.ListOfDispensers.Remove(dispenser);

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