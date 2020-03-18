using System.Collections.Generic;
using System.Threading.Tasks;
using WebApplication1.API.Domain.Models;
using WebApplication1.API.Domain.Services.Communication;
using WebApplication1.API.Resources;

namespace WebApplication1.API.Domain.Services
{
    public interface IDispService
    {
        Task<IEnumerable<Plan>> ListDatesAsync(DispSendToServerGET dispSendToServer);
        Task<DispResponse> SaveHistoryRecordAsync(Historia disp);
    }
}