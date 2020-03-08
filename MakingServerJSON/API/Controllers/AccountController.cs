using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using AutoMapper;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using WebApplication1.API.Domain.Models;
using WebApplication1.API.Domain.Services;
using WebApplication1.API.Resources;

namespace WebApplication1.API.Controllers
{
    [Route("api/[controller]")]
    public class AccountController : Controller
    {
        private readonly IDispenserService _DispenserService;
        private readonly IMapper _mapper;
        public AccountController(IDispenserService DispenserService, IMapper mapper)
        {
            _DispenserService = DispenserService;
            _mapper = mapper;
        }

        //Logowanie
        [HttpGet]
        public async Task<IEnumerable<DispenserResources>> GetDispenserAsync([FromBody] SentAccount sentAccount)
        {
            var disp = await _DispenserService.GetByLoginAndPassword(sentAccount.Login, sentAccount.Password);
            //var resource = _mapper.Map<Dispenser, DispenserResources>(disp);
            var resources = _mapper.Map<IEnumerable<Dispenser>, IEnumerable<DispenserResources>>(disp);

            return resources;
        }
    }
}