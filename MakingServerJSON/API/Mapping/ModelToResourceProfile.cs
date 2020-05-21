using AutoMapper;
using WebApplication1.API.Domain.Models;
using WebApplication1.API.Resources;

namespace WebApplication1.API.Mapping
{
    public class ModelToResourceProfile : Profile
    {
        public ModelToResourceProfile()
        {
            CreateMap<Dispenser, DispenserResources>();
            CreateMap<ListOfDispenser, DispenserResources>();
            CreateMap<Plan, ServResourcesToDisp>();
            CreateMap<Account, AccountCheck>();
            CreateMap<DispSendToServerPOST, Historia>();
        }
    }
}