using System.Collections.Generic;
using System.Threading.Tasks;
using AutoMapper;
using Microsoft.AspNetCore.Mvc;
using WebApplication1.API.Domain.Models;
using WebApplication1.API.Domain.Repositories;
using WebApplication1.API.Extensions;
using WebApplication1.API.Resources;

namespace WebApplication1.API.Controllers
{
    [Route("api/[controller]")]
    public class AccountController : Controller
    {
        private readonly IAccountService _accountService;
        private readonly IMapper _mapper;
        public AccountController(IAccountService accountService, IMapper mapper)
        {
            _accountService = accountService;
            _mapper = mapper;
        }

        [HttpPost("login")]
        public async Task<IEnumerable<DispenserResources>> GetListOfDispensersAsync([FromBody] SentAccount sentAccount)
        {
            var list = await _accountService.GetDispensersByLoginAndPassword(sentAccount.Login, sentAccount.Password);
            var resources = _mapper.Map<IEnumerable<ListOfDispenser>, IEnumerable<DispenserResources>>(list);

            return resources;
        }

        [HttpPost("register")]
        public async Task<IActionResult> RegisterAccountAsync([FromBody] AccountSendRegister accountSendRegister)
        {
            if (!ModelState.IsValid)
                return BadRequest(ModelState.GetErrorMessages());

            var accRegister = _mapper.Map<AccountSendRegister, Account>(accountSendRegister);

            var result = await _accountService.SaveAsync(accRegister);

            if (!result.Success)
                return Ok(result.Message);

            return Ok("0");
        }

        [HttpPost("check")]
        public async Task<AccountCheck> GetTypeOfAccountAsync([FromBody] AccountSendLogin accountSendLogin)
        {
            var acc = await _accountService.CheckAccountByPass(accountSendLogin.Login);
            var resources = _mapper.Map<Account, AccountCheck>(acc);

            return resources;
        }

        [HttpDelete]
        public async Task<IActionResult> RemoveAccount([FromBody] SentAccount sentAccount)
        {
            if (!ModelState.IsValid)
                return BadRequest(ModelState.GetErrorMessages());

            var result = await _accountService.DeleteAsync(sentAccount);

            if (!result.Success)
                return BadRequest(result.Message);

            return Ok();
        }
    }
}