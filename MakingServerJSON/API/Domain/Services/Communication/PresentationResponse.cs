using WebApplication1.API.Domain.Models;

namespace WebApplication1.API.Domain.Services.Communication
{
    public class PresentationResponse : BaseResponse<Presentation>
    {
        /// <summary>
        /// Creates a success response.
        /// </summary>
        /// <param name="presentation">Saved dispenser.</param>
        /// <returns>Response.</returns>
        public PresentationResponse(Presentation presentation) : base(presentation) {}

        /// <summary>
        /// Creates am error response.
        /// </summary>
        /// <param name="message">Error message.</param>
        /// <returns>Response.</returns>
        public PresentationResponse(string message) : base(message) {}
    }
}
