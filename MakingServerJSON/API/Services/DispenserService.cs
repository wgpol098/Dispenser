using System;
using System.Threading.Tasks;
using WebApplication1.API.Domain.Models;
using WebApplication1.API.Domain.Repositories;
using WebApplication1.API.Domain.Services;
using WebApplication1.API.Domain.Services.Communication;
using WebApplication1.API.Resources;

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
        public async Task<ListOfDispenser> SaveAsync(DispenserResource dispenserResource)
        {
            try
            {
                var result = await _dispenserRepository.AddAsync(dispenserResource);
                await _unitOfWork.CompleteAsync();

                return result;
            }
            catch (Exception)
            {
                return null;
            }
        }

        public async Task<bool> DeleteAsync(DispenserResource dispenserResource)
        {
            var existingDispenser = await _dispenserRepository.FindByLoginAndIdAsync(dispenserResource);

            if (existingDispenser == null)
                return false;

            try
            {
                await _dispenserRepository.Remove(existingDispenser);
                await _unitOfWork.CompleteAsync();

                return true;
            }
            catch (Exception)
            {
                return false;
            }
        }
    }
}