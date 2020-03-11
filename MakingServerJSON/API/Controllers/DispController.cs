using System;
using System.Collections.Generic;
using System.Threading.Tasks;
using AutoMapper;
using Microsoft.AspNetCore.Mvc;
using WebApplication1.API.Domain.Models;
using WebApplication1.API.Domain.Services;
using WebApplication1.API.Resources;

namespace WebApplication1.API.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class DispController : Controller
    {
        private readonly IDispService _dispService;
        private readonly IMapper _mapper;
        public DispController(IDispService disp, IMapper mapper)
        {
            _dispService = disp;
            _mapper = mapper;
        }

        [HttpGet]
        public async Task<IEnumerable<ServResourcesToDisp>> GetAllDatesFromPlan([FromBody] DispSendToServer dispSendToServer)
        {
            var disp = await _dispService.ListDatesAsync(dispSendToServer);
            try
            {
                //await _dispService.RemoveRecords(disp);
            }
            catch (Exception)
            {
                return null;
            }
            var resources = _mapper.Map<IEnumerable<Plan>, IEnumerable<ServResourcesToDisp>>(disp);

            return resources;
        }

        [HttpGet("debugplans")]
        public async Task<IEnumerable<Plan>> GetAllRecordsFromPlans()
        {
            var disp = await _dispService.ListAllRecordsFromPlans();
            return disp;
        }

        [HttpGet("debughistory")]
        public async Task<IEnumerable<Historia>> GetAllRecordsFromHistory()
        {
            var disp = await _dispService.ListAllRecordsFromHistory();
            return disp;
        }
    }
}