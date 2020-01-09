using System;
using Microsoft.AspNetCore.Mvc;

namespace WebApplicationMetody.API
{
    public class DispenserController : Controller
    {
        [Route("/api/Post/")]
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

        [Route("/api/Get/")]
        public ActionResult Get()
        {
            string s = System.IO.File.ReadAllText(@"Test.txt");
            int temp = Convert.ToInt32(s);
            return Content(temp.ToString());
        }

        [Route("/api/Display/")]
        public bool TestGet()
        {
            return true;
        }

        [Route("/api/GetInt/")]
        public int Get2()
        {
            string s = System.IO.File.ReadAllText(@"Test.txt");
            int temp = Convert.ToInt32(s);
            return temp;
        }

        [Route("/api/PostInt/")]
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