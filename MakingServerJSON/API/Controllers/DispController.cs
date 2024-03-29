﻿using System.Collections.Generic;
using System.Threading.Tasks;
using AutoMapper;
using Microsoft.AspNetCore.Mvc;
using WebApplication1.API.Domain.Models;
using WebApplication1.API.Domain.Services;
using WebApplication1.API.Extensions;
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
        public async Task<IEnumerable<ServResourcesToDisp>> GetAllDatesFromPlan([FromBody] DispSendToServerGET dispSendToServer)
        {
            return _mapper.Map<IEnumerable<Plan>, IEnumerable<ServResourcesToDisp>>(await _dispService.ListDatesAsync(dispSendToServer));
        }

        [HttpPost]
        public async Task<IActionResult> PostToHistory([FromBody] DispSendToServerPOST dispSendToServer)
        {
            if (!ModelState.IsValid) return BadRequest(ModelState.GetErrorMessages());
            var result = await _dispService.SaveHistoryRecordAsync(_mapper.Map<DispSendToServerPOST, Historia>(dispSendToServer));
            if (!result.Success) return BadRequest(result.Message);
            return Ok();
        }

        [HttpPost("PresentationPost")]
        public async Task<IActionResult> PrezentationPost ([FromBody] DispSendPres dispSendPres)
        {
            if (!ModelState.IsValid) return BadRequest(ModelState.GetErrorMessages());
            var result = await _dispService.ChangeRecordInPresentationTable(dispSendPres);
            if (!result.Success) return BadRequest(result.Message);
            return Ok();
        }

        [HttpGet("PresentationGet")]
        public async Task<IEnumerable<Presentation>> PresentationGet()
        {
            return await _dispService.ListPresentationGet();
        }
    }
}