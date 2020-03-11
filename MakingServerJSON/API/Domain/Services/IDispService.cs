using System.Collections.Generic;
using System.Threading.Tasks;
using WebApplication1.API.Domain.Models;
using WebApplication1.API.Resources;

namespace WebApplication1.API.Domain.Services
{
    public interface IDispService
    {
        Task<IEnumerable<Plan>> ListDatesAsync(DispSendToServer dispSendToServer);
        Task<bool> RemoveRecords(IEnumerable<Plan> disp);
        Task<IEnumerable<Plan>> ListAllRecordsFromPlans();
        Task<IEnumerable<Historia>> ListAllRecordsFromHistory();
    }
}
