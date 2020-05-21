using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using WebApplication1.API.Domain.Models;
using WebApplication1.API.Resources;

namespace WebApplication1.API.Domain.Repositories
{
    public interface IAndroidRepository
    {
        Task<IEnumerable<AndroidSendToAppByIdDisp>> ListAllRecordsForDispenserAsync(int androidSendIdDispenser);
        Task<IEnumerable<AndroidSendToAppByIdDispHistory>> ListAllRecordsInHistoryAsync(int androidSendIdDispenser);
        Task<AndroidSendToAppByIdRecord> ListAllRecordsInPlansByIdRecordAsync(int androidSendIdDispenser);
        Task AddAsync(Plan dispenser);
        Task<Dispenser> ReturnDispenserFromTable(int idDispenser);
        Task<Dispenser> FindDispenserAsync(int id);
        Task<Plan> FindPlanAsync(int idRecord); //Coś tu nie gra, potem sprawdzić dlaczego jest tylko 1 odwołanie
        Task<bool> Remove(int existingPlan);
        Task<bool> Update(AndroidSendPostUpdate existingPlan);
        void UpdateOkienka(Dispenser existingDispenser);
        Task<IEnumerable<AndroidSendToAppByIdDispDoctorHistory>> ListAllRecordsInHistoryPlanAsync(int idDispenser);
        Task<IEnumerable<AndroidSendToAppByIdDispDoctorPlan>> ListAllRecordsInDoctorPlanAsync(int idDispenser);
        Task<AndroidSendToAppByDispWindows> ListAllRecordsOfWindows(int idDispenser);
        Task<int> SendUpdateCounter(int androidSendIdDispenser);
    }
}
