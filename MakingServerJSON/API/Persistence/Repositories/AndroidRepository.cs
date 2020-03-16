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
    public class AndroidRepository : BaseRepository, IAndroidRepository
    {
        public AndroidRepository(AppDbContext context) : base(context)
        {
        }

        public async Task AddAsync(Plan dispenser)
        {
            await _context.Plans.AddAsync(dispenser);
        }

        public async Task<Dispenser> FindDispenser(int id)
        {
            return await _context.Dispensers.FindAsync(id);
        }

        public async Task<IEnumerable<AndroidSendToAppByIdDisp>> ListAllRecordsForDispenserAsync(int androidSendIdDispenser)
        {
            List<AndroidSendToAppByIdDisp> ReturnList = new List<AndroidSendToAppByIdDisp>();
            var TempListFromDatabase = await _context.Plans.Where(q => q.DispenserId == androidSendIdDispenser).ToListAsync();
            foreach (var q in TempListFromDatabase)
            {
                var temp2 = new AndroidSendToAppByIdDisp()
                {
                    Description = q.Opis,
                    Hour = q.DateAndTime.Hour,
                    Minutes = q.DateAndTime.Minute,
                    IdRecord = q.Id
                };
                ReturnList.Add(temp2);
            }
            return ReturnList;
        }

        public async Task<IEnumerable<AndroidSendToAppByIdDispHistory>> ListAllRecordsInHistoryAsync(int androidSendIdDispenser)
        {
            List<AndroidSendToAppByIdDispHistory> ReturnList = new List<AndroidSendToAppByIdDispHistory>();
            var TempListFromDatabase = await _context.History.Where(q => q.DispenserId == androidSendIdDispenser).ToListAsync();
            foreach (var q in TempListFromDatabase)
            {
                var temp2 = new AndroidSendToAppByIdDispHistory()
                {
                    DateAndTime = q.DateAndTime,
                    Flaga = q.Flaga,
                    Nr_Okienka = q.Nr_Okienka,
                    Opis = q.Opis
                };
                ReturnList.Add(temp2);
            }
            return ReturnList;
        }

        public async Task<AndroidSendToAppByIdRecord> ListAllRecordsInPlansByIdRecordAsync(int androidSendIdDispenser)
        {
            //Wyciaganie IdDispensera
            int IdDispenser = _context.Plans.FirstOrDefault(q => q.Id == androidSendIdDispenser).DispenserId;
            string Opis = _context.Plans.FirstOrDefault(q => q.Id == androidSendIdDispenser).Opis;

            //Przygotowanie danych
            List<Plan> lista = await _context.Plans.Where(q => q.DispenserId == IdDispenser).ToListAsync();
            lista = lista.Where(q => q.Opis == Opis).ToList();
            lista = lista.OrderBy(q => q.DateAndTime).ToList();

            int Period = 0;
            var temp = lista.FirstOrDefault(q => q.Id == androidSendIdDispenser);

            //Obliczanie danych
            if (lista.Count > 1)
            {
                var RoznicaCzasu = lista.ElementAt(1).DateAndTime - lista.ElementAt(0).DateAndTime;
                Period = RoznicaCzasu.Days * 24 + RoznicaCzasu.Hours;
            }

            //Pakowanie danych
            var temp2 = new AndroidSendToAppByIdRecord()
            {
                hour = temp.DateAndTime.Hour,
                minutes = temp.DateAndTime.Minute,
                description = temp.Opis,
                Count = lista.Count,
                Periodicity = Period
            };

            //Wysłanie danych
            return temp2;
        }

        public async Task<Dispenser> ReturnDispenserFromTable(int idDispenser)
        {
            return await _context.Dispensers.FirstOrDefaultAsync(q => q.DispenserId == idDispenser);
        }

        public void UpdateOkienka(Dispenser existingDispenser)
        {
            _context.Dispensers.Update(existingDispenser);
        }
    }
}