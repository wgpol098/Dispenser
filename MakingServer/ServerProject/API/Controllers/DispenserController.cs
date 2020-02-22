using System.Collections.Generic;
using System.Threading.Tasks;
using AutoMapper;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using WebApplication1.API.Domain.Models;
using WebApplication1.API.Domain.Services;
using WebApplication1.API.Resources;
using WebApplication1.API.Extensions;
using System;

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

            var categoryResource = _mapper.Map<Dispenser, DispenserResources>(result.Dispensers);
            return Ok(categoryResource);
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

            var categoryResource = _mapper.Map<Dispenser, DispenserResources>(result.Dispensers);
            return Ok(categoryResource);
        }

        [HttpDelete("{id}")]
        public async Task<IActionResult> DeleteAsync(int id)
        {
            var result = await _DispenserService.DeleteAsync(id);

            if (!result.Success)
                return BadRequest(result.Message);

            var categoryResource = _mapper.Map<Dispenser, DispenserResources>(result.Dispensers);
            return Ok(categoryResource);
        }

        //[Route("/api/Post/")]
        //public ActionResult Post()
        //{
        //    string s = System.IO.File.ReadAllText(@"Test.txt");
        //    int temp = Convert.ToInt32(s);
        //        if (temp == 0)
        //            temp = 1;
        //        else temp = 0;
        //        System.IO.File.WriteAllText(@"Test.txt", temp.ToString());
        //    return Content(temp.ToString());
        //}

        //[Route("/api/Get/")]
        //public ActionResult Get()
        //{
        //    string s = System.IO.File.ReadAllText(@"Test.txt");
        //    int temp = Convert.ToInt32(s);
        //    return Content(temp.ToString());
        //}

        //[Route("/api/Display/")]
        //public bool TestGet()
        //{
        //    return true;
        //}

        //[Route("/api/GetInt/")]
        //public int Get2()
        //{
        //    string s = System.IO.File.ReadAllText(@"Test.txt");
        //    int temp = Convert.ToInt32(s);
        //    return temp;
        //}

        //[Route("/api/PostInt/")]
        //public int Post2()
        //{
        //    string s = System.IO.File.ReadAllText(@"Test.txt");
        //    int temp = Convert.ToInt32(s);
        //    if (temp == 0)
        //        temp = 1;
        //    else temp = 0;
        //    System.IO.File.WriteAllText(@"Test.txt", temp.ToString());
        //    return temp;
        //}
    }
}