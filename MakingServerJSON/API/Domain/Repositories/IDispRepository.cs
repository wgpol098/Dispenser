using System.Collections.Generic;
using System.Threading.Tasks;
using WebApplication1.API.Domain.Models;
using WebApplication1.API.Resources;

namespace WebApplication1.API.Domain.Repositories
{
    public interface IDispRepository
    {
        Task<IEnumerable<Plan>> ListAllDatesAsync(DispSendToServerGET dispSendToServerGET);
        Task<IEnumerable<Presentation>> ListAllRecordsPresentationAsync();
        Task AddHistoryAsync(Historia historia);
        void ChangeRecordInPresentationTableAsync(Presentation dispSendPres);
        Task<Presentation> FindPrezRecordByNoWindowAsync(int NoWindow);
    }
}