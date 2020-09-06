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
            if (!ModelState.IsValid) return BadRequest(ModelState.GetErrorMessages());
            if ((await _DispenserService.SaveAsync(resource)) == null) return BadRequest();
            return Ok();
        }

        [HttpDelete]
        public async Task<IActionResult> DeleteAsync([FromBody] DispenserResource resource)
        {
            if (!(await _DispenserService.DeleteAsync(resource))) return BadRequest();
            return Ok();
        }
    }
}