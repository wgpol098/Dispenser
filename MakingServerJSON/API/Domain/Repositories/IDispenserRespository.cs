﻿using System.Collections.Generic;
using System.Threading.Tasks;
using WebApplication1.API.Domain.Models;

namespace WebApplication1.API.Domain.Repositories
{
    public interface IDispenserRepository
    {
        Task<IEnumerable<Dispenser>> ListAsync();
        Task AddAsync(Dispenser dispenser);
        Task<Dispenser> FindByIdAsync(int id);
        Task<Dispenser> GetByLoginAsync(string login);
        void Update(Dispenser dispenser);
        Task<bool> Remove(Dispenser category);
    }
}