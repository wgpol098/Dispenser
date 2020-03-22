using System.Threading.Tasks;
using WebApplication1.API.Domain.Services.Communication;
using WebApplication1.API.Resources;

namespace WebApplication1.API.Domain.Services
{
    public interface IDispenserService
    {
        Task<DispenserResponse> SaveAsync(DispenserResource dispenserResource);
        Task<bool> DeleteAsync(DispenserResource dispenserResource);
    }
}