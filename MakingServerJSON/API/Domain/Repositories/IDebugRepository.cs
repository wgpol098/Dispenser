using System.Collections.Generic;
using System.Threading.Tasks;
using WebApplication1.API.Domain.Models;

namespace WebApplication1.API.Domain.Repositories
{
    public interface IDebugRepository
    {
        Task<IEnumerable<Account>> FindAllRecordsInAccounts();
        Task<IEnumerable<Dispenser>> FindAllRecordsInDispensers();
        Task<IEnumerable<Historia>> FindAllRecordsInHistory();
        Task<IEnumerable<Plan>> FindAllRecordsInPlans();
        Task<IEnumerable<ListOfDispenser>> FindAllRecordsInListOfDispensers();
    }
}