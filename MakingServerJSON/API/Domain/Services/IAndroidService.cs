﻿using System.Collections.Generic;
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
        Task<IEnumerable<AndroidSendToAppGetDayInfo>> ListDayInfo(AndroidSendGetDayInfo androidSendGetDayInfo);
        Task<IEnumerable<AndroidSendToAppCallendarInfo>> ListCallendarInfo(AndroidSendCallendarInfo androidSendCallendarInfo);
        Task<bool> UpdateAsync(AndroidSendPostUpdate resource);
        Task<bool> DeleteAsync(AndroidSendIdRecord androidSendIdRecord);
        Task<IEnumerable<AndroidSendToAppByIdDispDoctorHistory>> ListHistoryPlanAsync(int idDispenser);
        Task<IEnumerable<AndroidSendToAppByIdDispDoctorPlan>> ListDoctorPlanAsync(int idDispenser);
        Task<AndroidSendToAppByDispWindows> ListWindows(int idDispenser);
        Task<DispenserUpdateCounter> IncCounterAsync(int idDispenser, bool increment);
    }
}
