using System.Collections.Generic;
using System.Threading.Tasks;
using WebApplication1.API.Domain.Models;
using WebApplication1.API.Domain.Services;

namespace WebApplication1.API.Services
{
    public class DispenserService : IDispenserService
    {
        private readonly IDispenserService _dispenserRepository;

        public DispenserService(IDispenserService dispenserRepository)
        {
            this._dispenserRepository = dispenserRepository;
        }

        public async Task<IEnumerable<Dispenser>> ListAsync()
        {
            return await _dispenserRepository.ListAsync();
        }
    }
}
