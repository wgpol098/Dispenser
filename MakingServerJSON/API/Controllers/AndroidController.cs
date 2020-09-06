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
        public async Task<IEnumerable<AndroidSendToAppByIdDisp>> GetByIdDispenser([FromBody] AndroidSendIdDispenser androidSendIdDispenser)
        {
            return await _androidService.ListPlansAsync(androidSendIdDispenser.IdDispenser);
        }

        [HttpPost("GetPlanRecord")]
        public async Task<AndroidSendToAppByIdRecord> GetByIdRecord([FromBody] AndroidSendIdRecord androidSendIdRecord)
        {
            return await _androidService.ListPlansByRecAsync(androidSendIdRecord.IdRecord);
        }

        [HttpPost("GetHistory")]
        public async Task<IEnumerable<AndroidSendToAppByIdDispHistory>> GetByIdDispenserHistory([FromBody] AndroidSendIdDispenser androidSendIdDispenser)
        {
            return await _androidService.ListHistoryAsync(androidSendIdDispenser.IdDispenser);
        }

        [HttpPost("GetDoctorHistory")]
        public async Task<IEnumerable<AndroidSendToAppByIdDispDoctorHistory>> GetByIdDispenserHistoryPlan([FromBody] AndroidSendIdDispenser androidSendIdDispenser)
        {
            return await _androidService.ListHistoryPlanAsync(androidSendIdDispenser.IdDispenser);
        }

        [HttpPost("GetDoctorPlan")]
        public async Task<IEnumerable<AndroidSendToAppByIdDispDoctorPlan>> GetByIdDispenserDoctorPlan([FromBody] AndroidSendIdDispenser androidSendIdDispenser)
        {
            return await _androidService.ListDoctorPlanAsync(androidSendIdDispenser.IdDispenser);
        }

        [HttpPost("GetWindows")]
        public async Task<AndroidSendToAppByDispWindows> GetByIdDispenserWindows([FromBody] AndroidSendIdDispenser androidSendIdDispenser)
        {
            return await _androidService.ListWindows(androidSendIdDispenser.IdDispenser);
        }

        [HttpPost("GetDayInfo")]
        public async Task<IEnumerable<AndroidSendToAppGetDayInfo>> GetDayInfo([FromBody] AndroidSendGetDayInfo androidSendGetDayInfo)
        {
            return await _androidService.ListDayInfo(androidSendGetDayInfo);
        }

        [HttpPost("GetCallendarInfo")]
        public async Task<IEnumerable<AndroidSendToAppCallendarInfo>> GetCallendarInfo([FromBody] AndroidSendCallendarInfo androidSendCallendarInfo)
        {
            return await _androidService.ListCallendarInfo(androidSendCallendarInfo);
        }

        [HttpPost("UpdateCounter")]
        public async Task<DispenserUpdateCounter> UpdateCounterAsync([FromBody] AndroidSendIdDispenser androidSendIdDispenser)
        {
            if (!ModelState.IsValid) return new DispenserUpdateCounter() { NoUpdate = -1 };
            return await _androidService.IncCounterAsync(androidSendIdDispenser.IdDispenser, false);
        }

        [HttpPost]
        public async Task<IActionResult> PostAsync([FromBody] AndroidSendPost androidSendPost)
        {
            if (!ModelState.IsValid) return BadRequest(ModelState.GetErrorMessages());

            DateTime dateAndTime = new DateTime(DateTime.Now.Year, DateTime.Now.Month, DateTime.Now.Day,
                androidSendPost.Hour, androidSendPost.Minutes, 0).AddDays(androidSendPost.Days);

            if (androidSendPost.Hour < DateTime.Now.Hour) dateAndTime = dateAndTime.AddDays(1);

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
                await _androidService.IncCounterAsync(androidSendPost.IdDispenser, true);
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
            if (!ModelState.IsValid) return BadRequest(ModelState.GetErrorMessages());
            if (!(await _androidService.UpdateAsync(resource))) return BadRequest();
            return Ok();
        }

        [HttpDelete]
        public async Task<IActionResult> DeleteAsync([FromBody] AndroidSendIdRecord androidSendIdRecord)
        {
            if (!ModelState.IsValid) return BadRequest(ModelState.GetErrorMessages());
            if (!(await _androidService.DeleteAsync(androidSendIdRecord))) return BadRequest();
            return Ok();
        }
    }
}