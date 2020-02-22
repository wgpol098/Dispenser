using System.Collections.Generic;
using System.Threading.Tasks;
using WebApplication1.API.Domain.Models;
using WebApplication1.API.Domain.Services.Communication;

namespace WebApplication1.API.Domain.Services
{
    public interface IDispenserService
    {
        Task<IEnumerable<Dispenser>> ListAsync();
        Task<DispenserResponse> SaveAsync(Dispenser dispenser);
        Task<DispenserResponse> UpdateAsync(int id, Dispenser dispenser);
        Task<DispenserResponse> DeleteAsync(int id);
    }
}