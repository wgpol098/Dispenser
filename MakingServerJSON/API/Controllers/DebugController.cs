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
            return await _debugService.ListAllRecordsFromPlans();
        }

        [HttpGet("history")]
        public async Task<IEnumerable<Historia>> GetAllRecordsFromHistory()
        {
            return await _debugService.ListAllRecordsFromHistory();
        }

        [HttpGet("dispensers")]
        public async Task<IEnumerable<Dispenser>> GetAllRecordsFromDispensers()
        {
            return await _debugService.ListAllRecordsFromDispensers();
        }

        [HttpGet("accounts")]
        public async Task<IEnumerable<Account>> GetAllRecordsFromAccounts()
        {
            return await _debugService.ListAllRecordsFromAccounts();
        }

        [HttpGet("listofdispensers")]
        public async Task<IEnumerable<ListOfDispenser>> GetAllRecordsFromListOfDispensers()
        {
            return await _debugService.ListAllRecordsFromListOfDispensers();
        }
    }
}