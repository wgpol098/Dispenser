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

        public async Task AddHistoryAsync(Historia hist)
        {
            var plans = await _context.Plans.Where(q => q.IdDispenser == hist.IdDispenser).ToListAsync();

            Historia historia = new Historia()
            {
                IdDispenser = hist.IdDispenser,
                NoWindow = hist.NoWindow,
                Flag = hist.Flag,
                DateAndTime = hist.DateAndTime,
                Description = plans.FirstOrDefault(q => q.NoWindow == hist.NoWindow).Description
            };

            try
            {
                await _context.History.AddAsync(historia);

                foreach(var q in plans)
                {
                    if(q.DateAndTime < DateTime.Now)
                        _context.Plans.Remove(q);
                }

                await _context.SaveChangesAsync();
            }
            catch (Exception)
            {
                return;
            }
        }

        public async Task<IEnumerable<Plan>> ListAllDatesAsync(DispSendToServerGET dispSendToServer)
        {
            return await _context.Plans.Where(q => q.IdDispenser == dispSendToServer.IdDispenser).ToListAsync();
        }
    }
}