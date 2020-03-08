using System.Collections.Generic;
using System.Threading.Tasks;
using AutoMapper;
using Microsoft.AspNetCore.Mvc;
using WebApplication1.API.Domain.Models;
using WebApplication1.API.Domain.Services;
using WebApplication1.API.Resources;
using WebApplication1.API.Extensions;

namespace WebApplication1.API.Controllers
{
    
    [Route("/api/[controller]/")]
    public class DispenserController : Controller
    {
        private readonly IDispenserService _DispenserService;
        private readonly IMapper _mapper;

        public DispenserController(IDispenserService DispenserService, IMapper mapper)
        {
            _DispenserService = DispenserService;
            _mapper = mapper;
        }

        [HttpGet]
        public async Task<IEnumerable<DispenserResources>> GetAllAsync()
        {
            var disp = await _DispenserService.ListAsync();
            var resources = _mapper.Map<IEnumerable<Dispenser>, IEnumerable<DispenserResources>>(disp);

            return resources;
        }

        [HttpPost]
        public async Task<IActionResult> PostAsync([FromBody] SaveDispenserResource resource)
        {
            if (!ModelState.IsValid)
                return BadRequest(ModelState.GetErrorMessages());

            var dispenser = _mapper.Map<SaveDispenserResource, Dispenser>(resource);

            var result = await _DispenserService.SaveAsync(dispenser);

            if (!result.Success)
                return BadRequest(result.Message);

            var dispenserResource = _mapper.Map<Dispenser, DispenserResources>(result.Resource);
            return Ok(dispenserResource);
        }

        [HttpPut("{id}")]
        public async Task<IActionResult> PutAsync(int id, [FromBody] SaveDispenserResource resource)
        {
            if (!ModelState.IsValid)
                return BadRequest(ModelState.GetErrorMessages());

            var category = _mapper.Map<SaveDispenserResource, Dispenser>(resource);
            var result = await _DispenserService.UpdateAsync(id, category);

            if (!result.Success)
                return BadRequest(result.Message);

            var dispenserResource = _mapper.Map<Dispenser, DispenserResources>(result.Resource);
            return Ok(dispenserResource);
        }

        [HttpDelete("{id}")]
        public async Task<IActionResult> DeleteAsync(int id)
        {
            var result = await _DispenserService.DeleteAsync(id);

            if (!result.Success)
                return BadRequest(result.Message);

            var dispenserResource = _mapper.Map<Dispenser, DispenserResources>(result.Resource);
            return Ok(dispenserResource);
        }
    }
}