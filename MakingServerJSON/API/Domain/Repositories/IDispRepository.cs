using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using WebApplication1.API.Domain.Models;
using WebApplication1.API.Resources;

namespace WebApplication1.API.Domain.Repositories
{
    public interface IDispRepository
    {
        Task<IEnumerable<Plan>> ListAllDatesAsync(DispSendToServer dispSendToServer);
        Task AddAsync(DispSendToServer dispenser);
        Task<Plan> FindByIdAsync(int id);
        Task<bool> RemoveAsync(Plan d);
        Task<IEnumerable<Plan>> FindAllRecordsInPlans();
        Task<IEnumerable<Historia>> FindAllRecordsInHistory();
    }
}
