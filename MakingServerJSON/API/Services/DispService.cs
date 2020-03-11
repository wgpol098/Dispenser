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

        public async Task<IEnumerable<Historia>> ListAllRecordsFromHistory()
        {
            return await _dispRepository.FindAllRecordsInHistory();
        }

        public async Task<IEnumerable<Plan>> ListAllRecordsFromPlans()
        {
            return await _dispRepository.FindAllRecordsInPlans();
        }

        public async Task<IEnumerable<Plan>> ListDatesAsync(DispSendToServer dispSendToServer)
        {
            //var existingDispenser = await _dispRepository.FindByIdAsync(dispSendToServer.DispenserId);

            //if (existingDispenser == null)
            //    return null;

            try
            {
                await _dispRepository.AddAsync(dispSendToServer);
                //await _dispRepository.RemoveAsync();
                await _unitOfWork.CompleteAsync();

                return await _dispRepository.ListAllDatesAsync(dispSendToServer);
            }
            catch (Exception)
            {
                return null;
            }
            
        }

        public async Task<bool> RemoveRecords(IEnumerable<Plan> disp)
        {
            try
            {
                foreach (var d in disp)
                    await _dispRepository.RemoveAsync(d);
            }
            catch (Exception)
            {
                return false;
            }
            return true;
        }
    }
}

//Oddzielić dodawanie rekordów na 2 metody, który zaczyna się od controllera (tak samo z remove)