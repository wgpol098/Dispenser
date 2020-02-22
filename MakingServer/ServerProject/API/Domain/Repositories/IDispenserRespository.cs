using System.Collections.Generic;
using System.Threading.Tasks;
using WebApplication1.API.Domain.Models;

namespace WebApplication1.API.Domain.Repositories
{
    public interface IDispenserRepository
    {
        Task<IEnumerable<Dispenser>> ListAsync();
        Task AddAsync(Dispenser dispenser);
        Task<Dispenser> FindByIdAsync(int id);
        void Update(Dispenser dispenser);
        void Remove(Dispenser category);
    }
}