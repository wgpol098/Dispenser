using System.Threading.Tasks;
using WebApplication1.API.Domain.Models;
using WebApplication1.API.Resources;

namespace WebApplication1.API.Domain.Repositories
{
    public interface IDispenserRepository
    {
        Task<ListOfDispenser> AddAsync(DispenserResource dispenserResource);
        Task<ListOfDispenser> FindByLoginAndIdAsync(DispenserResource dispenserResource);
        Task<bool> Remove(ListOfDispenser dispenser);
    }
}