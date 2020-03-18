using System.Collections.Generic;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Mvc;
using WebApplication1.API.Domain.Models;
using WebApplication1.API.Domain.Services;

namespace WebApplication1.API.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class DebugController : Controller
    {
        private readonly IDebugService _debugService;
        public DebugController(IDebugService debug)
        {
            _debugService = debug;
        }

        [HttpGet("plans")]
        public async Task<IEnumerable<Plan>> GetAllRecordsFromPlans()
        {
            var plan = await _debugService.ListAllRecordsFromPlans();
            return plan;
        }

        [HttpGet("history")]
        public async Task<IEnumerable<Historia>> GetAllRecordsFromHistory()
        {
            var hist = await _debugService.ListAllRecordsFromHistory();
            return hist;
        }

        [HttpGet("dispensers")]
        public async Task<IEnumerable<Dispenser>> GetAllRecordsFromDispensers()
        {
            var disp = await _debugService.ListAllRecordsFromDispensers();
            return disp;
        }

        [HttpGet("accounts")]
        public async Task<IEnumerable<Account>> GetAllRecordsFromAccounts()
        {
            var acc = await _debugService.ListAllRecordsFromAccounts();
            return acc;
        }
    }
}