using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using WebApplication1.API.Domain.Models;

namespace WebApplication1.API.Domain.Services.Communication
{
    public class DispResponse : BaseResponse<Historia>
    {
        /// <summary>
        /// Creates a success response.
        /// </summary>
        /// <param name="dispenser">Saved dispenser.</param>
        /// <returns>Response.</returns>
        public DispResponse(Historia dispenser) : base(dispenser)
        { }

        /// <summary>
        /// Creates am error response.
        /// </summary>
        /// <param name="message">Error message.</param>
        /// <returns>Response.</returns>
        public DispResponse(string message) : base(message)
        { }
    }
}
