using System.Threading.Tasks;
using AutoMapper;
using Microsoft.AspNetCore.Mvc;
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

        [HttpPost]
        public async Task<IActionResult> PostAsync([FromBody] DispenserResource resource)
        {
            if (!ModelState.IsValid)
                return BadRequest(ModelState.GetErrorMessages());

            var result = await _DispenserService.SaveAsync(resource);

            if (!result.Success)
                return BadRequest(result.Message);

            return Ok();
        }

        [HttpDelete]
        public async Task<IActionResult> DeleteAsync([FromBody] DispenserResource resource)
        {
            var result = await _DispenserService.DeleteAsync(resource);

            if (!result)
                return BadRequest();

            return Ok();
        }
    }
}