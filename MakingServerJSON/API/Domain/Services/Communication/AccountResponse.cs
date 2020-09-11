using WebApplication1.API.Domain.Models;

namespace WebApplication1.API.Domain.Services.Communication
{
    public class AccountResponse : BaseResponse<Account>
    {
        /// <summary>
        /// Creates a success response.
        /// </summary>
        /// <param name="dispenser">Saved account.</param>
        /// <returns>Response.</returns>
        public AccountResponse(Account dispenser) : base(dispenser){}

        /// <summary>
        /// Creates am error response.
        /// </summary>
        /// <param name="message">Error message.</param>
        /// <returns>Response.</returns>
        public AccountResponse(string message) : base(message){}
    }
}
