using System;
using System.Collections.Generic;
using System.Threading.Tasks;
using WebApplication1.API.Domain.Models;
using WebApplication1.API.Domain.Repositories;
using WebApplication1.API.Domain.Services;
using WebApplication1.API.Domain.Services.Communication;

namespace WebApplication1.API.Services
{
    public class DispenserService : IDispenserService
    {
        private readonly IDispenserRepository _dispenserRepository;
        private readonly IUnitOfWork _unitOfWork;

        public DispenserService(IDispenserRepository dispenserRepository, IUnitOfWork unitOfWork)
        {
            _dispenserRepository = dispenserRepository;
            _unitOfWork = unitOfWork;
        }

        public async Task<IEnumerable<Dispenser>> ListAsync()
        {
            return await _dispenserRepository.ListAsync();
        }

        public async Task<DispenserResponse> SaveAsync(Dispenser dispenser)
        {
            try
            {
                await _dispenserRepository.AddAsync(dispenser);
                await _unitOfWork.CompleteAsync();

                return new DispenserResponse(dispenser);
            }
            catch (Exception ex)
            {
                return new DispenserResponse($"An error occurred when saving the dispenser: {ex.Message}");
            }
        }

        public async Task<DispenserResponse> UpdateAsync(int id, Dispenser category)
        {
            var existingCategory = await _dispenserRepository.FindByIdAsync(id);

            if (existingCategory == null)
                return new DispenserResponse("Dispensera nie odnaleziono.");

            existingCategory.Name = category.Name;

            try
            {
                _dispenserRepository.Update(existingCategory);
                await _unitOfWork.CompleteAsync();

                return new DispenserResponse(existingCategory);
            }
            catch (Exception ex)
            {
                return new DispenserResponse($"An error occurred when updating the dispenser: {ex.Message}");
            }
        }

        public async Task<DispenserResponse> DeleteAsync(int id)
        {
            var existingCategory = await _dispenserRepository.FindByIdAsync(id);

            if (existingCategory == null)
                return new DispenserResponse("Dispensera nie odnaleziono.");

            try
            {
                _dispenserRepository.Remove(existingCategory);
                await _unitOfWork.CompleteAsync();

                return new DispenserResponse(existingCategory);
            }
            catch (Exception ex)
            {
                return new DispenserResponse($"An error occurred when deleting the dispenser: {ex.Message}");
            }
        }
    }
}
