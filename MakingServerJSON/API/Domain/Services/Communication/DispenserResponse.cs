using WebApplication1.API.Domain.Models;

namespace WebApplication1.API.Domain.Services.Communication
{
    public class DispenserResponse : BaseResponse<Dispenser>
    {
        /// <summary>
        /// Creates a success response.
        /// </summary>
        /// <param name="dispenser">Saved dispenser.</param>
        /// <returns>Response.</returns>
        public DispenserResponse(Dispenser dispenser) : base(dispenser) {}

        /// <summary>
        /// Creates am error response.
        /// </summary>
        /// <param name="message">Error message.</param>
        /// <returns>Response.</returns>
        public DispenserResponse(string message) : base(message) {}
    }
}
