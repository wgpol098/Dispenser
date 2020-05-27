﻿using Microsoft.EntityFrameworkCore;
using Microsoft.EntityFrameworkCore.Internal;
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

        public async Task<IEnumerable<AndroidSendToAppByIdDisp>> ListAllRecordsForDispenserAsync(int androidSendIdDispenser)
        {
            List<AndroidSendToAppByIdDisp> ReturnList = new List<AndroidSendToAppByIdDisp>();
            var TempListFromDatabase = await _context.Plans.Where(q => q.IdDispenser == androidSendIdDispenser).ToListAsync();
            TempListFromDatabase.Sort((q,p) => q.DateAndTime.CompareTo(p.DateAndTime));

            bool jakisbool = false;
            foreach (var q in TempListFromDatabase)
            {
                jakisbool = false;
                var temp2 = new AndroidSendToAppByIdDisp()
                {
                    Description = q.Description,
                    Hour = q.DateAndTime.Hour,
                    Minutes = q.DateAndTime.Minute,
                    IdRecord = q.Id
                };
                foreach(var p in ReturnList)
                {
                    if (temp2.Description == p.Description || (temp2.Hour == p.Hour && temp2.Minutes == p.Minutes))
                        jakisbool = true;
                }
                if(!jakisbool)
                    ReturnList.Add(temp2);
            }
            
            return ReturnList;
        }

        public async Task<IEnumerable<AndroidSendToAppByIdDispDoctorPlan>> ListAllRecordsInDoctorPlanAsync(int idDispenser)
        {
            List<AndroidSendToAppByIdDispDoctorPlan> ReturnList = new List<AndroidSendToAppByIdDispDoctorPlan>();
            List<Plan> TempList = new List<Plan>();
            List<string> ListOfUniqueDescription = new List<string>();

            var TempListFromDatabaseHistory = await _context.History.Where(q => q.IdDispenser == idDispenser).ToListAsync();
            var TempListFromDatabasePlans = await _context.Plans.Where(q => q.IdDispenser == idDispenser).ToListAsync();
            int counter = 0;
            bool jakisbool = false;

            TempListFromDatabaseHistory.Sort((q, p) => q.DateAndTime.CompareTo(p.DateAndTime));
            TempListFromDatabasePlans.Sort((q, p) => q.DateAndTime.CompareTo(p.DateAndTime));

            foreach (var q in TempListFromDatabasePlans)
            {
                if (!ListOfUniqueDescription.Contains(q.Description))
                {
                    ListOfUniqueDescription.Add(q.Description);

                    foreach (var p in TempList)
                    {
                        if (q.Description == p.Description || (q.DateAndTime.Hour == p.DateAndTime.Hour && q.DateAndTime.Minute == p.DateAndTime.Minute))
                            jakisbool = true;
                    }
                    if (!jakisbool)
                        TempList.Add(q);

                }
            }

            int Period = 0;
            DateTime dateTime;
            List<ListOfDate> temp = new List<ListOfDate>();

            foreach (var q in ListOfUniqueDescription)
            {
                temp.Clear();

                foreach (var p in TempListFromDatabaseHistory)
                {
                    if (p.Description == q && p.Flag <= 0)
                    {
                        ListOfDate listOfDate = new ListOfDate()
                        {
                            Date = p.DateAndTime.Year.ToString() + "-" + p.DateAndTime.Month.ToString() + "-" + p.DateAndTime.Day.ToString() + "-" +
                            p.DateAndTime.Hour.ToString() + "-" + p.DateAndTime.Minute.ToString()
                        };
                        temp.Add(listOfDate);
                        dateTime = p.DateAndTime;

                        if (temp.Count == 2)
                        {
                            var RoznicaCzasu = p.DateAndTime - dateTime;
                            Period = RoznicaCzasu.Days * 24 + RoznicaCzasu.Hours;
                        }
                    }
                }

                if (TempListFromDatabasePlans.Where(p => p.Description == q).Count() >= 2)
                {
                    var RoznicaCzasu = TempListFromDatabasePlans.Where(p => p.Description == q).ElementAt(1).DateAndTime -
                        TempListFromDatabasePlans.Where(p => p.Description == q).ElementAt(0).DateAndTime;
                    Period = RoznicaCzasu.Days * 24 + RoznicaCzasu.Hours;
                }

                string start = TempListFromDatabasePlans.FirstOrDefault(p => p.Description == q).DateAndTime.Year + "-" +
                        TempListFromDatabasePlans.FirstOrDefault(p => p.Description == q).DateAndTime.Month + "-" +
                        TempListFromDatabasePlans.FirstOrDefault(p => p.Description == q).DateAndTime.Day;
                string fh = (TempListFromDatabasePlans.Where(p => p.Description == q).FirstOrDefault().DateAndTime.Hour + "-" 
                    + TempListFromDatabasePlans.Where(p => p.Description == q).FirstOrDefault().DateAndTime.Minute).ToString();

                int end = TempListFromDatabasePlans.LastOrDefault().DateAndTime.Day - TempListFromDatabasePlans.FirstOrDefault().DateAndTime.Day;

                var temp2 = new AndroidSendToAppByIdDispDoctorPlan()
                {
                    Description = q,
                    Start = start,
                    TabDidnttake = new List<ListOfDate>(temp),
                    FirstHour = fh,
                    Periodicity = Period,
                    DaysLeft = end,
                    IdRecord = TempList.ElementAt(counter).Id
                };
                counter++;
                ReturnList.Add(temp2);
            }

            return ReturnList;
        }

        public async Task<IEnumerable<AndroidSendToAppByIdDispHistory>> ListAllRecordsInHistoryAsync(int androidSendIdDispenser)
        {
            List<AndroidSendToAppByIdDispHistory> ReturnList = new List<AndroidSendToAppByIdDispHistory>();
            var TempListFromDatabase = await _context.History.Where(q => q.IdDispenser == androidSendIdDispenser).ToListAsync();
            foreach (var q in TempListFromDatabase)
            {
                var temp = new AndroidSendToAppByIdDispHistory()
                {
                    DateAndTime = q.DateAndTime,
                    Flag = q.Flag,
                    NoWindow = q.NoWindow,
                    Description = q.Description
                };
                ReturnList.Add(temp);
            }
            return ReturnList;
        }

        public async Task<IEnumerable<AndroidSendToAppByIdDispDoctorHistory>> ListAllRecordsInHistoryPlanAsync(int idDispenser)
        {
            List<AndroidSendToAppByIdDispDoctorHistory> ReturnList = new List<AndroidSendToAppByIdDispDoctorHistory>();
            List<string> ListOfUniqueDescription = new List<string>();

            var TempListFromDatabase = await _context.History.Where(q => q.IdDispenser == idDispenser).ToListAsync();
            TempListFromDatabase.Sort((q, p) => q.DateAndTime.CompareTo(p.DateAndTime));

            foreach (var q in TempListFromDatabase)
            {
                if (!ListOfUniqueDescription.Contains(q.Description)) 
                    ListOfUniqueDescription.Add(q.Description);
            }

            int Period = 0;
            DateTime dateTime;
            List<ListOfDate> temp = new List<ListOfDate>();

            foreach (var q in ListOfUniqueDescription)
            {
                temp.Clear();
                foreach(var p in TempListFromDatabase)
                {
                    if (p.Description == q && p.Flag <= 0)
                    {
                        ListOfDate listOfDate = new ListOfDate()
                        {
                            Date = p.DateAndTime.Year.ToString() + "-" + p.DateAndTime.Month.ToString() + "-" + p.DateAndTime.Day.ToString() + "-" +
                            p.DateAndTime.Hour.ToString() + "-" + p.DateAndTime.Minute.ToString()
                        };
                        temp.Add(listOfDate);
                        dateTime = p.DateAndTime;

                        if(temp.Count == 2)
                        {
                            var RoznicaCzasu = p.DateAndTime - dateTime;
                            Period = RoznicaCzasu.Days * 24 + RoznicaCzasu.Hours;
                        }
                    }
                }

                string start = "", end = "";

                if(temp.Count > 0)
                {
                    start = TempListFromDatabase.FirstOrDefault(p => p.Description == q).DateAndTime.Year + "-" +
                        TempListFromDatabase.FirstOrDefault(p => p.Description == q).DateAndTime.Month + "-" +
                        TempListFromDatabase.FirstOrDefault(p => p.Description == q).DateAndTime.Day;

                    end = TempListFromDatabase.LastOrDefault(p => p.Description == q).DateAndTime.Year + "-" +
                        TempListFromDatabase.LastOrDefault(p => p.Description == q).DateAndTime.Month + "-" +
                        TempListFromDatabase.LastOrDefault(p => p.Description == q).DateAndTime.Day;
                }

                var temp2 = new AndroidSendToAppByIdDispDoctorHistory()
                {
                    Description = q,
                    Start = start,
                    End = end,
                    TabDidnttake = new List<ListOfDate>(temp),
                    FirstHour = (TempListFromDatabase.Where(p => p.Description == q).FirstOrDefault().DateAndTime.Hour + "-" +
                    TempListFromDatabase.Where(p => p.Description == q).FirstOrDefault().DateAndTime.Minute).ToString(),
                    Periodicity = Period,
                    Count = temp.Count
                };

                ReturnList.Add(temp2);
            }

            return ReturnList;
        }

        public async Task<AndroidSendToAppByIdRecord> ListAllRecordsInPlansByIdRecordAsync(int androidSendIdDispenser)
        {
            //Wyciaganie IdDispensera
            int IdDispenser = _context.Plans.FirstOrDefault(q => q.Id == androidSendIdDispenser).IdDispenser;
            string Opis = _context.Plans.FirstOrDefault(q => q.Id == androidSendIdDispenser).Description;

            //Przygotowanie danych
            List<Plan> lista = await _context.Plans.Where(q => q.IdDispenser == IdDispenser).ToListAsync();
            lista = lista.Where(q => q.Description == Opis).ToList();
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
                Hour = temp.DateAndTime.Hour,
                Minutes = temp.DateAndTime.Minute,
                Days = (DateTime.Now - temp.DateAndTime).Days,
                Description = temp.Description,
                Count = lista.Count,
                Periodicity = Period
            };

            //Wysłanie danych
            return temp2;
        }

        public async Task<AndroidSendToAppByDispWindows> ListAllRecordsOfWindows(int idDispenser)
        {
            List<AndroidSendToAppByDispWindows> ReturnList = new List<AndroidSendToAppByDispWindows>();
            var TempListFromDatabase = await _context.Dispensers.Where(q => q.IdDispenser == idDispenser).ToListAsync();

            var Free = TempListFromDatabase.FirstOrDefault().NoWindow.Count(x => x == '0');
            var Occupied = TempListFromDatabase.FirstOrDefault().NoWindow.Count(x => x == '1');

            return new AndroidSendToAppByDispWindows()
            {
                FreeWindows = Free,
                OccupiedWindows = Occupied
            };
        }

        public async Task<bool> Remove(int existingPlan)
        {
            Plan plan = await _context.Plans.FirstOrDefaultAsync(q => q.Id == existingPlan);
            Dispenser FindDispenserOkienka = await _context.Dispensers.FirstOrDefaultAsync(q => q.IdDispenser == plan.IdDispenser);

            var FoundPlans = await _context.Plans.Where(q => q.IdDispenser == plan.IdDispenser).ToListAsync();
            FoundPlans = FoundPlans.Where(q => q.Description == plan.Description).ToList();

            //Próba wykasowania
            try
            {
                System.Text.StringBuilder temp = new System.Text.StringBuilder(FindDispenserOkienka.NoWindow);
                foreach (var q in FoundPlans)
                {
                    if (q.NoWindow != -1)
                        temp[q.NoWindow - 1] = '0';
                }
                string str = temp.ToString();
                FindDispenserOkienka.NoWindow = str;

                foreach (var q in FoundPlans)
                {
                    _context.Plans.Remove(q);
                }

                await UpdateCounter(plan.IdDispenser, true); //Nie pewny
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
            return await _context.Dispensers.FirstOrDefaultAsync(q => q.IdDispenser == idDispenser);
        }

        public async Task<bool> Update(AndroidSendPostUpdate existingPlan)
        {
            DateTime dateAndTime = new DateTime(DateTime.Now.Year, DateTime.Now.Month, DateTime.Now.Day,
                existingPlan.Hour, existingPlan.Minutes, 0);

            dateAndTime = dateAndTime.AddDays(existingPlan.Days);

            if (existingPlan.Hour < DateTime.Now.Hour)
                dateAndTime = dateAndTime.AddDays(1);

            var FindPlan = await _context.Plans.FirstOrDefaultAsync(q => q.Id == existingPlan.IdRecord);

            //Nowy lub update
            Dispenser FindDispenserOkienka = 
                await _context.Dispensers.FirstOrDefaultAsync(q => q.IdDispenser == FindPlan.IdDispenser);

            //Próba wykasowania
            try
            {
                await Remove(existingPlan.IdRecord);
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
                    for (int j = 0; j < FindDispenserOkienka.NoWindow.Length; j++)
                    {
                        if (FindDispenserOkienka.NoWindow[j] == '0')
                        {
                            System.Text.StringBuilder temp = new System.Text.StringBuilder(FindDispenserOkienka.NoWindow);
                            temp[j] = '1';
                            string str = temp.ToString();
                            Plan plan = new Plan()
                            {
                                DateAndTime = dateAndTime,
                                IdDispenser = FindPlan.IdDispenser,
                                Description = existingPlan.Description,
                                NoWindow = j + 1
                            };

                            await AddAsync(plan);

                            FindDispenserOkienka.NoWindow = str;

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
                            IdDispenser = FindPlan.IdDispenser,
                            Description = existingPlan.Description,
                            NoWindow = -1
                        };

                        await AddAsync(plan);

                        dateAndTime.AddHours(existingPlan.Periodicity);
                    }

                    SprawdzCzyWolne = false;
                }

                await UpdateCounter(FindPlan.IdDispenser, true);  //W tym miejscu może wywalić, bo FindPlan.IdDispenser jest nie pewny
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

        public async Task<DispenserUpdateCounter> UpdateCounter(int androidSendIdDispenser, bool increment)
        {
            try
            {
                Dispenser disp = await _context.Dispensers.FirstOrDefaultAsync(q => q.IdDispenser == androidSendIdDispenser);
                if (increment)
                {
                    disp.NoUpdate++;
                    await _context.SaveChangesAsync();
                }

                return new DispenserUpdateCounter() { NoUpdate = disp.NoUpdate };
            }
            catch (Exception)
            {
                return new DispenserUpdateCounter() { NoUpdate = -1 };
            }
        }

        public async Task<DispenserUpdateCounter> UpdateCounterFindByIdRecord(int androidSendIdRecord)
        {
            try
            {
                Plan plan = await _context.Plans.FirstOrDefaultAsync(q => q.Id == androidSendIdRecord);
                Dispenser disp = await _context.Dispensers.FirstOrDefaultAsync(q => q.IdDispenser == plan.IdDispenser);

                disp.NoUpdate++;
                await _context.SaveChangesAsync();

                return new DispenserUpdateCounter() { NoUpdate = disp.NoUpdate };
            }
            catch (Exception)
            {
                return new DispenserUpdateCounter() { NoUpdate = -1 };
            }
        }
    }
}