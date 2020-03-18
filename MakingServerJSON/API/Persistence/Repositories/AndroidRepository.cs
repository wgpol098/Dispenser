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

        public async Task<Dispenser> FindDispenserAsync(int id)
        {
            return await _context.Dispensers.FindAsync(id);
        }

        public async Task<Plan> FindPlanAsync(int id)
        {
            return await _context.Plans.FindAsync(id);
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

        public async Task<bool> Remove(int existingPlan)
        {
            Plan plan = await _context.Plans.FirstOrDefaultAsync(q => q.Id == existingPlan);
            Dispenser FindDispenserOkienka = await _context.Dispensers.FirstOrDefaultAsync(q => q.DispenserId == plan.DispenserId);

            var FoundPlans = await _context.Plans.Where(q => q.DispenserId == plan.DispenserId).ToListAsync();
            FoundPlans = FoundPlans.Where(q => q.Opis == plan.Opis).ToList();

            //Próba wykasowania
            try
            {
                System.Text.StringBuilder temp = new System.Text.StringBuilder(FindDispenserOkienka.Nr_Okienka);
                foreach (var q in FoundPlans)
                {
                    if (q.Nr_Okienka != -1)
                        temp[q.Nr_Okienka - 1] = '0';
                }
                string str = temp.ToString();
                FindDispenserOkienka.Nr_Okienka = str;

                foreach (var q in FoundPlans)
                {
                    _context.Plans.Remove(q);
                }

                await _context.SaveChangesAsync();
                UpdateOkienka(FindDispenserOkienka);

                return true;
            }
            catch (Exception)
            {
                return false;
            }
        }

        public async Task<Dispenser> ReturnDispenserFromTable(int idDispenser)
        {
            return await _context.Dispensers.FirstOrDefaultAsync(q => q.DispenserId == idDispenser);
        }

        public async Task<bool> Update(AndroidSendPostUpdate existingPlan)
        {
            DateTime dateAndTime = new DateTime(DateTime.Now.Year, DateTime.Now.Month, DateTime.Now.Day,
                existingPlan.Hour, existingPlan.Minutes, 0);

            if (existingPlan.Hour < DateTime.Now.Hour)
                dateAndTime.AddDays(1);

            var FindPlan = await _context.Plans.FirstOrDefaultAsync(q => q.Id == existingPlan.IdRecord);

            //Nowy lub update
            Dispenser FindDispenserOkienka = 
                await _context.Dispensers.FirstOrDefaultAsync(q => q.DispenserId == FindPlan.DispenserId);

            //Próba wykasowania
            try
            {
                var result = await Remove(existingPlan.IdRecord);
            }
            catch (Exception)
            {
                return false;
            }

            try
            {
                bool SprawdzCzyWolne = false;

                for (int i = 0; i < existingPlan.Count; i++)
                {
                    for (int j = 0; j < FindDispenserOkienka.Nr_Okienka.Length; j++)
                    {
                        if (FindDispenserOkienka.Nr_Okienka[j] == '0')
                        {
                            System.Text.StringBuilder temp = new System.Text.StringBuilder(FindDispenserOkienka.Nr_Okienka);
                            temp[j] = '1';
                            string str = temp.ToString();
                            Plan plan = new Plan()
                            {
                                DateAndTime = dateAndTime,
                                DispenserId = FindPlan.DispenserId,
                                Opis = existingPlan.Description,
                                Nr_Okienka = j + 1
                            };

                            await AddAsync(plan);

                            FindDispenserOkienka.Nr_Okienka = str;

                            UpdateOkienka(FindDispenserOkienka);

                            dateAndTime = dateAndTime.AddHours(existingPlan.Periodicity);
                            SprawdzCzyWolne = true;
                            break;
                        }
                    }

                    if (SprawdzCzyWolne == false)
                    {
                        Plan plan = new Plan()
                        {
                            DateAndTime = dateAndTime,
                            DispenserId = FindPlan.DispenserId,
                            Opis = existingPlan.Description,
                            Nr_Okienka = -1
                        };

                        await AddAsync(plan);

                        dateAndTime.AddHours(existingPlan.Periodicity);
                    }

                    SprawdzCzyWolne = false;
                }

                await _context.SaveChangesAsync();

                return true;
            }
            catch (Exception)
            {
                return false;
            }
        }

        public void UpdateOkienka(Dispenser existingDispenser)
        {
            _context.Dispensers.Update(existingDispenser);
        }
    }
}