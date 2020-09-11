using WebApplication1.API.Domain.Models;

namespace WebApplication1.API.Domain.Services.Communication
{
    public class AndroidResponse : BaseResponse<Plan>
    {
        /// <summary>
        /// Creates a success response.
        /// </summary>
        /// <param name="dispenser">Saved account.</param>
        /// <returns>Response.</returns>
        public AndroidResponse(Plan dispenser) : base(dispenser){}

        /// <summary>
        /// Creates am error response.
        /// </summary>
        /// <param name="message">Error message.</param>
        /// <returns>Response.</returns>
        public AndroidResponse(string message) : base(message){}
    }
}
