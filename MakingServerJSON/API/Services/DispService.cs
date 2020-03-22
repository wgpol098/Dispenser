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
    public class DispService : IDispService
    {
        private readonly IDispRepository _dispRepository;
        private readonly IUnitOfWork _unitOfWork;

        public DispService(IDispRepository dispRepository, IUnitOfWork unitOfWork)
        {
            _dispRepository = dispRepository;
            _unitOfWork = unitOfWork;
        }

        public async Task<IEnumerable<Plan>> ListDatesAsync(DispSendToServerGET dispSendToServer)
        {
            try
            {
                return await _dispRepository.ListAllDatesAsync(dispSendToServer);
            }
            catch (Exception)
            {
                return null;
            }
        }

        public async Task<DispResponse> SaveHistoryRecordAsync(Historia historia)
        {
            try
            {
                await _dispRepository.AddHistoryAsync(historia);
                await _unitOfWork.CompleteAsync();

                return new DispResponse(historia);
            }
            catch (Exception ex)
            {
                return new DispResponse($"An error occurred when saving the record of Historia: {ex.Message}");
            }
        }
    }
}