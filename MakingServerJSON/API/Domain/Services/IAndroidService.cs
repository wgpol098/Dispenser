using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using WebApplication1.API.Domain.Models;
using WebApplication1.API.Domain.Services.Communication;
using WebApplication1.API.Resources;

namespace WebApplication1.API.Domain.Services
{
    public interface IAndroidService
    {
        Task<IEnumerable<AndroidSendToAppByIdDisp>> ListPlansAsync(int androidSendIdDispenser);
        Task<IEnumerable<AndroidSendToAppByIdDispHistory>> ListHistoryAsync(int androidSendIdDispenser);
        Task<AndroidSendToAppByIdRecord> ListPlansByRecAsync(int androidSendIdRecord);
        Task<AndroidResponse> SaveAsync(Plan dispenser);
        Task<Dispenser> FindOkienka(int idDispenser);
        Task<DispenserResponse> UpdateDispenserOkienkaAsync(string v, int id);
    }
}
