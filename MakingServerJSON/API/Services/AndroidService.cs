using System;
using System.Collections.Generic;
using System.Threading.Tasks;
using WebApplication1.API.Domain.Models;
using WebApplication1.API.Domain.Repositories;
using WebApplication1.API.Domain.Services;
using WebApplication1.API.Domain.Services.Communication;
using WebApplication1.API.Resources;

namespace WebApplication1.API.Services
{
    public class AndroidService : IAndroidService
    {
        private readonly IAndroidRepository _androidRepository;
        private readonly IUnitOfWork _unitOfWork;

        public AndroidService(IAndroidRepository androidRepository, IUnitOfWork unitOfWork)
        {
            _androidRepository = androidRepository;
            _unitOfWork = unitOfWork;
        }

        public async Task<bool> DeleteAsync(AndroidSendIdRecord androidSendIdRecord)
        {
            try
            {
                await _androidRepository.Remove(androidSendIdRecord.IdRecord);
                await _unitOfWork.CompleteAsync();

                return true;
            }
            catch (Exception)
            {
                return false;
            }
        }

        public async Task<Dispenser> FindOkienka(int idDispenser)
        {
            return await _androidRepository.ReturnDispenserFromTable(idDispenser);
        }

        public async Task<IEnumerable<AndroidSendToAppByIdDispHistory>> ListHistoryAsync(int androidSendIdDispenser)
        {
            return await _androidRepository.ListAllRecordsInHistoryAsync(androidSendIdDispenser);
        }

        public async Task<IEnumerable<AndroidSendToAppByIdDispDoctorHistory>> ListHistoryPlanAsync(int idDispenser)
        {
            return await _androidRepository.ListAllRecordsInHistoryPlanAsync(idDispenser);
        }

        public async Task<IEnumerable<AndroidSendToAppByIdDisp>> ListPlansAsync(int androidSendIdDispenser)
        {
            return await _androidRepository.ListAllRecordsForDispenserAsync(androidSendIdDispenser);
        }

        public async Task<AndroidSendToAppByIdRecord> ListPlansByRecAsync(int androidSendIdRecord)
        {
            return await _androidRepository.ListAllRecordsInPlansByIdRecordAsync(androidSendIdRecord);
        }

        public async Task<IEnumerable<AndroidSendToAppByIdDispDoctorPlan>> ListDoctorPlanAsync(int idDispenser)
        {
            return await _androidRepository.ListAllRecordsInDoctorPlanAsync(idDispenser);
        }

        public async Task<AndroidSendToAppByDispWindows> ListWindows(int idDispenser)
        {
            return await _androidRepository.ListAllRecordsOfWindows(idDispenser);
        }

        public async Task<AndroidResponse> SaveAsync(Plan dispenser)
        {
            try
            {
                await _androidRepository.AddAsync(dispenser);
                await _unitOfWork.CompleteAsync();

                return new AndroidResponse(dispenser);
            }
            catch (Exception ex)
            {
                return new AndroidResponse($"An error occurred when saving the plan: {ex.Message}");
            }
        }

        public async Task<bool> UpdateAsync(AndroidSendPostUpdate resource)
        {
            try
            {
                var result = await _androidRepository.Update(resource);
                await _unitOfWork.CompleteAsync();

                return true;
            }
            catch (Exception)
            {
                return false;
            }
        }

        public async Task<DispenserResponse> UpdateDispenserOkienkaAsync(string v, int id)
        {
            var existingDispenser = await _androidRepository.FindDispenserAsync(id);

            if (existingDispenser == null)
                return new DispenserResponse("Can't find dispenser with that id.");

            try
            {
                existingDispenser.NoWindow = v;
                _androidRepository.UpdateOkienka(existingDispenser);
                await _unitOfWork.CompleteAsync();

                return new DispenserResponse(existingDispenser);
            }
            catch (Exception ex)
            {
                return new DispenserResponse($"An error occurred when updating the dispenser: {ex.Message}");
            }
        }
    }
}