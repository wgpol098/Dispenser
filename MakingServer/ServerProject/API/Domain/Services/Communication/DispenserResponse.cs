using WebApplication1.API.Domain.Models;

namespace WebApplication1.API.Domain.Services.Communication
{
    public class DispenserResponse : BaseResponse
    {
        public Dispenser Dispensers { get; private set; }

        private DispenserResponse(bool success, string message, Dispenser dispenser) : base(success, message)
        {
            Dispensers = dispenser;
        }

        /// <summary>
        /// Creates a success response.
        /// </summary>
        /// <param name="dispenser">Saved category.</param>
        /// <returns>Response.</returns>
        public DispenserResponse(Dispenser dispenser) : this(true, string.Empty, dispenser)
        { }

        /// <summary>
        /// Creates am error response.
        /// </summary>
        /// <param name="message">Error message.</param>
        /// <returns>Response.</returns>
        public DispenserResponse(string message) : this(false, message, null)
        { }
    }
}
