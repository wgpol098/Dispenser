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
            var plans = await _context.Plans.Where(q => q.DispenserId == hist.DispenserId).ToListAsync();

            Historia historia = new Historia()
            {
                DispenserId = hist.DispenserId,
                Nr_Okienka = hist.Nr_Okienka,
                Flaga = hist.Flaga,
                DateAndTime = hist.DateAndTime,
                Opis = plans.FirstOrDefault(q => q.Nr_Okienka == hist.Nr_Okienka).Opis
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
            return await _context.Plans.Where(q => q.DispenserId == dispSendToServer.DispenserId).ToListAsync();
        }
    }
}