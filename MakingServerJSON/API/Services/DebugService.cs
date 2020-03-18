using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using WebApplication1.API.Domain.Models;
using WebApplication1.API.Domain.Repositories;
using WebApplication1.API.Domain.Services;

namespace WebApplication1.API.Services
{
    public class DebugService : IDebugService
    {
        private readonly IDebugRepository _debugRepository;

        public DebugService(IDebugRepository debugRepository)
        {
            _debugRepository = debugRepository;
        }
        public async Task<IEnumerable<Account>> ListAllRecordsFromAccounts()
        {
            return await _debugRepository.FindAllRecordsInAccounts();
        }

        public async Task<IEnumerable<Dispenser>> ListAllRecordsFromDispensers()
        {
            return await _debugRepository.FindAllRecordsInDispensers();
        }

        public async Task<IEnumerable<Historia>> ListAllRecordsFromHistory()
        {
            return await _debugRepository.FindAllRecordsInHistory();
        }

        public async Task<IEnumerable<Plan>> ListAllRecordsFromPlans()
        {
            return await _debugRepository.FindAllRecordsInPlans();
        }
    }
}
