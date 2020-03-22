using System.Collections.Generic;
using System.Threading.Tasks;
using WebApplication1.API.Domain.Models;

namespace WebApplication1.API.Domain.Services
{
    public interface IDebugService
    {
        Task<IEnumerable<Plan>> ListAllRecordsFromPlans();
        Task<IEnumerable<Historia>> ListAllRecordsFromHistory();
        Task<IEnumerable<Dispenser>> ListAllRecordsFromDispensers();
        Task<IEnumerable<Account>> ListAllRecordsFromAccounts();
        Task<IEnumerable<ListOfDispenser>> ListAllRecordsFromListOfDispensers();
    }
}
