using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using WebApplication1.API.Domain.Models;
using WebApplication1.Domain.Services;

namespace WebApplication1.API.Controllers
{
    
    //[Route("/api/[controller]/")]
    public class DispenserController : Controller
    {
        bool TurnON = false;
        /*private readonly IDispenserInterface _DispenserService;

        public DispenserController(IDispenserInterface DispenserService)
        {
            _DispenserService = DispenserService;
        }

        [HttpGet]
        public async Task<IEnumerable<Dispenser>> GetAllAsync()
        {
            var categories = await _DispenserService.ListAsync();
            return categories;
        }*/

        [Route("/api/[controller]/Post/")]
        public ActionResult Post()
        {
            string s = System.IO.File.ReadAllText(@"Test.txt");
            int temp = Convert.ToInt32(s);
                if (temp == 0)
                    temp = 1;
                else temp = 0;
                System.IO.File.WriteAllText(@"Test.txt", temp.ToString());
            return Content(temp.ToString());
        }
        
        [Route("/api/[controller]/Get/")]
        public ActionResult Get()
        {
            string s = System.IO.File.ReadAllText(@"Test.txt");
            int temp = Convert.ToInt32(s);
            return Content(temp.ToString());
        }

        [Route("/api/[controller]/Display/")]
        public bool TestGet()
        {
            return true;
        }

        [Route("/api/[controller]/GetInt/")]
        public int Get2()
        {
            string s = System.IO.File.ReadAllText(@"Test.txt");
            int temp = Convert.ToInt32(s);
            return temp;
        }

        [Route("/api/[controller]/PostInt/")]
        public int Post2()
        {
            string s = System.IO.File.ReadAllText(@"Test.txt");
            int temp = Convert.ToInt32(s);
            if (temp == 0)
                temp = 1;
            else temp = 0;
            System.IO.File.WriteAllText(@"Test.txt", temp.ToString());
            return temp;
        }
    }
}