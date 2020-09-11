using WebApplication1.API.Persistence.Contexts;

namespace WebApplication1.API.Persistence.Repositories
{
    public class BaseRepository
    {
        protected readonly AppDbContext _context;

        public BaseRepository(AppDbContext context) { _context = context; }
    }
}
