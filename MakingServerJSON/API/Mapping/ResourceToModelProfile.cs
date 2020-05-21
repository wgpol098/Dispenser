﻿using AutoMapper;
using WebApplication1.API.Domain.Models;
using WebApplication1.API.Resources;

namespace WebApplication1.API.Mapping
{
    public class ResourceToModelProfile : Profile
    {
        public ResourceToModelProfile()
        {
            CreateMap<DispenserResource, Dispenser>();
            CreateMap<AccountCheck, Account>();
            CreateMap<AccountSendRegister, Account>();
            CreateMap<DispSendToServerPOST, Historia>();
        }
    }
}
