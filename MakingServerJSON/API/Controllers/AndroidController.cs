using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using AutoMapper;
using Microsoft.AspNetCore.Http;
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

        [HttpGet("GetPlan")]
        public async Task<IEnumerable<AndroidSendToAppByIdDisp>> GetByIdDispenser([FromBody] AndroidSendIdDispenser androidSendIdDispenser){
            var disp = await _androidService.ListPlansAsync(androidSendIdDispenser.IdDispenser);
            //Można potem przerobić na mapę, jeżeli zdecydujemy się na te same pola modeli
            //var resources = _mapper.Map<IEnumerable<Plan>, IEnumerable<AndroidSendToAppByIdDisp>>(disp);

            return disp;
        }

        [HttpGet("GetPlanRecord")]
        public async Task<AndroidSendToAppByIdRecord> GetByIdRecord([FromBody] AndroidSendIdRecord androidSendIdRecord)
        {
            var disp = await _androidService.ListPlansByRecAsync(androidSendIdRecord.IdRecord);

            return disp;
        }

        [HttpGet("GetHistory")]
        public async Task<IEnumerable<AndroidSendToAppByIdDispHistory>> GetByIdDispenserHistory([FromBody] AndroidSendIdDispenser androidSendIdDispenser)
        {
            var disp = await _androidService.ListHistoryAsync(androidSendIdDispenser.IdDispenser);

            return disp;
        }

        //ADD
        [HttpPost]
        public async Task<IActionResult> PostAsync([FromBody] AndroidSendPost androidSendPost)
        {
            if (!ModelState.IsValid)
                return BadRequest(ModelState.GetErrorMessages());

            DateTime dateAndTime = new DateTime(DateTime.Now.Year, DateTime.Now.Month, DateTime.Now.Day,
                androidSendPost.Hour, androidSendPost.Minutes, 0);

            if (androidSendPost.Hour < DateTime.Now.Hour)
                dateAndTime.AddDays(1);

            Dispenser FindDispenserOkienka = await _androidService.FindOkienka(androidSendPost.IdDispenser);

            try
            {
                bool SprawdzCzyWolne = false;

                for(int i = 0; i < androidSendPost.Count; i++)
                {
                    for (int j = 0; j<FindDispenserOkienka.Nr_Okienka.Length; j++)
                    {
                        if(FindDispenserOkienka.Nr_Okienka[j] == '0') {
                            System.Text.StringBuilder temp = new System.Text.StringBuilder(FindDispenserOkienka.Nr_Okienka);
                            temp[j] = '1';
                            string str = temp.ToString();
                            Plan plan = new Plan()
                            {
                                DateAndTime = dateAndTime,
                                DispenserId = androidSendPost.IdDispenser,
                                Opis = androidSendPost.Opis,
                                Nr_Okienka = j + 1
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
                            DispenserId = androidSendPost.IdDispenser,
                            Opis = androidSendPost.Opis,
                            Nr_Okienka = -1
                        };

                        await _androidService.SaveAsync(plan);

                        dateAndTime.AddHours(androidSendPost.Periodicity);
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
        /*
        //UPDATE
        [HttpPut("{id}")]
        public async Task<IActionResult> PutAsync(int id, [FromBody] SaveDispenserResource resource)
        {
            if (!ModelState.IsValid)
                return BadRequest(ModelState.GetErrorMessages());

            var mapp = _mapper.Map<SaveDispenserResource, Dispenser>(resource);
            var result = await _androidService.UpdateAsync(id, mapp);

            if (!result.Success)
                return BadRequest(result.Message);

            var dispenserResource = _mapper.Map<Dispenser, DispenserResources>(result.Resource);
            return Ok(dispenserResource);
        }

        //DELETE
        [HttpDelete("{id}")]
        public async Task<IActionResult> DeleteAsync(int id)
        {
            var result = await _androidService.DeleteAsync(id);

            if (!result.Success)
                return BadRequest(result.Message);

            var dispenserResource = _mapper.Map<Dispenser, DispenserResources>(result.Resource);
            return Ok(dispenserResource);
        }*/
    }
}
// Zrobić PUT i DELETE, metodę POST można na pewno skrócić (Ale to potem)