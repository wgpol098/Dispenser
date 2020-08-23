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

            Historia historia;

            try
            {
                historia = new Historia()
                {
                    IdDispenser = hist.IdDispenser,
                    NoWindow = hist.NoWindow,
                    Flag = hist.Flag,
                    DateAndTime = hist.DateAndTime,
                    Description = plans.FirstOrDefault(q => q.NoWindow == hist.NoWindow).Description
                };
            }
            catch (Exception) //Catch tymczasowy, później do usunięcia
            {
                historia = new Historia()
                {
                    IdDispenser = hist.IdDispenser,
                    NoWindow = hist.NoWindow,
                    Flag = hist.Flag,
                    DateAndTime = hist.DateAndTime,
                    Description = "Siema Hubert"
                };
            }

            try
            {
                await _context.History.AddAsync(historia);

                foreach(var q in plans)
                {
                    if(q.DateAndTime < DateTime.Now)
                        _context.Plans.Remove(q);
                }

                await UpdateCounter(hist.IdDispenser);
                await _context.SaveChangesAsync();
            }
            catch (Exception)
            {
                return;
            }
        }

        public void ChangeRecordInPresentationTableAsync(Presentation presentation)
        {
            _context.Presentations.Update(presentation);
        }

        public async Task<Presentation> FindPrezRecordByNoWindowAsync(int NoWindow)
        {
            return await _context.Presentations.FirstOrDefaultAsync(q => q.Id == NoWindow);
        }

        public async Task<IEnumerable<Plan>> ListAllDatesAsync(DispSendToServerGET dispSendToServer)
        {
            return await _context.Plans.Where(q => q.IdDispenser == dispSendToServer.IdDispenser).ToListAsync();
        }

        public async Task<IEnumerable<Presentation>> ListAllRecordsPresentationAsync()
        {
            return await _context.Presentations.ToListAsync();
        }

        //Powtórzenie metody z AndroidRep - najwyżej później usunąć
        public async Task<bool> UpdateCounter(int androidSendIdDispenser)
        {
            try
            {
                Dispenser disp = await _context.Dispensers.FirstOrDefaultAsync(q => q.IdDispenser == androidSendIdDispenser);
                disp.NoUpdate++;

                await _context.SaveChangesAsync();

                return true;
            }
            catch (Exception)
            {
                return false;
            }
        }
    }
}