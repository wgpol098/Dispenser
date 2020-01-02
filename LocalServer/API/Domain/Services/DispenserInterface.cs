using System.Collections.Generic;
using System.Threading.Tasks;
using WebApplication1.API.Domain.Models;

namespace WebApplication1.API.Domain.Services
{
    public interface IDispenserService
    {
        Task<IEnumerable<Dispenser>> ListAsync();
    }
}