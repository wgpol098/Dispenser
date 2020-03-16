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
    public class DispRepository : BaseRepository, IDispRepository
    {
        public DispRepository(AppDbContext context) : base(context)
        {
        }

        public async Task AddAsync(DispSendToServer dispenser)
        {
            Plan plan = await _context.Plans.FirstOrDefaultAsync(q => q.DispenserId == dispenser.DispenserId);
            Historia historia = new Historia()
            {
                DispenserId = dispenser.DispenserId,
                Nr_Okienka = dispenser.Nr_Okienka,
                Flaga = dispenser.Flaga,
                DateAndTime = dispenser.dateTime,
                Opis = plan.Opis
            };
            await _context.History.AddAsync(historia);
        }

        public async Task<IEnumerable<Plan>> ListAllDatesAsync(DispSendToServer dispSendToServer)
        {
            return await _context.Plans.Where(q => q.DispenserId == dispSendToServer.DispenserId).ToListAsync();
        }

        public async Task<Plan> FindByIdAsync(int id)
        {
            return await _context.Plans.FindAsync(id);
        }

        //REMOVE TABELA PLANS
        public async Task<bool> RemoveAsync(Plan d)
        {
            if (d == null)
                return false;

            _context.Plans.Remove(d);

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

        public async Task<IEnumerable<Plan>> FindAllRecordsInPlans()
        {
            return await _context.Plans.ToListAsync();
        }

        public async Task<IEnumerable<Historia>> FindAllRecordsInHistory()
        {
            return await _context.History.ToListAsync();
        }
    }
}