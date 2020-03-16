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
        Task<Dispenser> FindDispenser(int id);
        void UpdateOkienka(Dispenser existingDispenser);
    }
}
