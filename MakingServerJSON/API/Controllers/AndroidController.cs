using System;
using System.Collections.Generic;
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
    public class AndroidController : Controller
    {
        private readonly IAndroidService _androidService;
        private readonly IMapper _mapper;

        public AndroidController(IAndroidService androidService, IMapper mapper)
        {
            _androidService = androidService;
            _mapper = mapper;
        }

        [HttpPost("GetPlan")]
        public async Task<IEnumerable<AndroidSendToAppByIdDisp>> GetByIdDispenser([FromBody] AndroidSendIdDispenser androidSendIdDispenser){
            var disp = await _androidService.ListPlansAsync(androidSendIdDispenser.IdDispenser);
            return disp;
        }

        [HttpPost("GetPlanRecord")]
        public async Task<AndroidSendToAppByIdRecord> GetByIdRecord([FromBody] AndroidSendIdRecord androidSendIdRecord)
        {
            var disp = await _androidService.ListPlansByRecAsync(androidSendIdRecord.IdRecord);
            return disp;
        }

        [HttpPost("GetHistory")]
        public async Task<IEnumerable<AndroidSendToAppByIdDispHistory>> GetByIdDispenserHistory([FromBody] AndroidSendIdDispenser androidSendIdDispenser)
        {
            var disp = await _androidService.ListHistoryAsync(androidSendIdDispenser.IdDispenser);
            return disp;
        }

        [HttpPost("GetDoctorHistory")]
        public async Task<IEnumerable<AndroidSendToAppByIdDispDoctorHistory>> GetByIdDispenserHistoryPlan([FromBody] AndroidSendIdDispenser androidSendIdDispenser)
        {
            var disp = await _androidService.ListHistoryPlanAsync(androidSendIdDispenser.IdDispenser);
            return disp;
        }

        [HttpPost("GetDoctorPlan")]
        public async Task<IEnumerable<AndroidSendToAppByIdDispDoctorPlan>> GetByIdDispenserDoctorPlan([FromBody] AndroidSendIdDispenser androidSendIdDispenser)
        {
            var disp = await _androidService.ListDoctorPlanAsync(androidSendIdDispenser.IdDispenser);
            return disp;
        }

        [HttpPost("GetWindows")]
        public async Task<AndroidSendToAppByDispWindows> GetByIdDispenserWindows([FromBody] AndroidSendIdDispenser androidSendIdDispenser)
        {
            var disp = await _androidService.ListWindows(androidSendIdDispenser.IdDispenser);
            return disp;
        }

        [HttpPost]
        public async Task<IActionResult> PostAsync([FromBody] AndroidSendPost androidSendPost)
        {
            if (!ModelState.IsValid)
                return BadRequest(ModelState.GetErrorMessages());

            DateTime dateAndTime = new DateTime(DateTime.Now.Year, DateTime.Now.Month, DateTime.Now.Day,
                androidSendPost.Hour, androidSendPost.Minutes, 0);

            dateAndTime.AddDays(androidSendPost.Days);

            if (androidSendPost.Hour < DateTime.Now.Hour)
                dateAndTime.AddDays(1);

            Dispenser FindDispenserOkienka = await _androidService.FindOkienka(androidSendPost.IdDispenser);

            try
            {
                bool SprawdzCzyWolne = false;

                for(int i = 0; i < androidSendPost.Count; i++)
                {
                    for (int j = 0; j<FindDispenserOkienka.NoWindow.Length; j++)
                    {
                        if(FindDispenserOkienka.NoWindow[j] == '0') {
                            System.Text.StringBuilder temp = new System.Text.StringBuilder(FindDispenserOkienka.NoWindow);
                            temp[j] = '1';
                            string str = temp.ToString();
                            Plan plan = new Plan()
                            {
                                DateAndTime = dateAndTime,
                                IdDispenser = androidSendPost.IdDispenser,
                                Description = androidSendPost.Description,
                                NoWindow = j + 1
                            };

                            await _androidService.SaveAsync(plan);

                            var result = await _androidService.UpdateDispenserOkienkaAsync(str, FindDispenserOkienka.Id);
                            if(!result.Success)
                                return BadRequest(result.Message);

                            dateAndTime = dateAndTime.AddHours(androidSendPost.Periodicity);
                            SprawdzCzyWolne = true;
                            break;
                        }
                    }

                    if(SprawdzCzyWolne == false)
                    {
                        Plan plan = new Plan()
                        {
                            DateAndTime = dateAndTime,
                            IdDispenser = androidSendPost.IdDispenser,
                            Description = androidSendPost.Description,
                            NoWindow = -1
                        };

                        await _androidService.SaveAsync(plan);

                        dateAndTime = dateAndTime.AddHours(androidSendPost.Periodicity);
                    }

                    SprawdzCzyWolne = false;
                }
            }
            catch (Exception)
            {
                return BadRequest();
            }
            return Ok();
        }
        
        [HttpPut]
        public async Task<IActionResult> PutAsync([FromBody] AndroidSendPostUpdate resource)
        {
            if (!ModelState.IsValid)
                return BadRequest(ModelState.GetErrorMessages());

            var result = await _androidService.UpdateAsync(resource);

            if (!result)
                return BadRequest();

            return Ok();
        }

        [HttpDelete]
        public async Task<IActionResult> DeleteAsync([FromBody] AndroidSendIdRecord androidSendIdRecord)
        {
            if (!ModelState.IsValid)
                return BadRequest(ModelState.GetErrorMessages());

            var result = await _androidService.DeleteAsync(androidSendIdRecord);

            if (!result)
                return BadRequest();

            return Ok();
        }
    }
}