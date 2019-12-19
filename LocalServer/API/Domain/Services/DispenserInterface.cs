using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using WebApplication1.API.Domain.Models;

namespace WebApplication1.Domain.Services
{
    public interface IDispenserInterface
    {
        Task<IEnumerable<Dispenser>> ListAsync();
    }
}
